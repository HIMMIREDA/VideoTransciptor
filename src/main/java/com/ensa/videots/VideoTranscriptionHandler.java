package com.ensa.videots;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoTranscriptionHandler extends Thread {
    private Stage stage;
    private YoutubeDownloader youtubeDownloaderThread = null;
    private String transcriptionLanguage;
    private static final Map<String, String> mapOfLanguages = new HashMap<>();
    private String filePath;

    static {
        mapOfLanguages.put("english", "en");
        mapOfLanguages.put("spanish", "es");
        mapOfLanguages.put("french", "fr");
    }


    public VideoTranscriptionHandler(String videoUrl, String filePath, Stage stage, String language) {
        if (videoUrl != null) {
            this.youtubeDownloaderThread = new YoutubeDownloader(videoUrl, stage, this);
            youtubeDownloaderThread.setDaemon(true);
        }
        this.filePath = filePath;
        this.stage = stage;
        this.transcriptionLanguage = language;
    }

    @Override
    public void run() {
        if (!interrupted()) {
            try {
                if (youtubeDownloaderThread != null) {
                    youtubeDownloaderThread.start();
                    synchronized (this) {
                        wait();
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    VideoTranscriptionLoadingController controller = (VideoTranscriptionLoadingController) PageNavigator.loadPage(PageNavigator.VIDEOTRANSCRIPTIONLOADINGPAGE);
                    controller.addLoader("Step 2/2", "Your video is on transcription process ...");
                }
            });
            try {
                // transcript using the downloaded file name or using the path of existing file
                getTranscription(youtubeDownloaderThread != null ? youtubeDownloaderThread.getDownloadedVideoFileName() : filePath);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.showToast(stage, e.getMessage(), Toast.TOAST_ERROR);
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    PageNavigator.loadPage(PageNavigator.VIDEOTRANSCRIPTIONREADYPAGE);
                }
            });
        }
    }

    private String upload(String path) throws Exception {
        Dotenv dotenv = Dotenv.configure().directory("./src/main/java").load();
        VideoTranscriptItem transcript = new VideoTranscriptItem();
        Gson gson = new Gson();

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/upload"))
                .headers("Authorization", dotenv.get("ASSEMBLY_AI_API_KEY"), "Transfer-Encoding", "chunked")
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(path)))
                .build();


        CompletableFuture<HttpResponse<String>> postResponse = HttpClient.newBuilder()
                .build()
                .sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

        transcript = gson.fromJson(postResponse.get().body(), VideoTranscriptItem.class);


        return transcript.getUpload_url();

    }


    public void getTranscription(String filePath) throws Exception {
        Dotenv dotenv = Dotenv.configure().directory("./src/main/java").load();
        VideoTranscriptItem transcript = new VideoTranscriptItem();
        System.out.println(java.net.URLDecoder.decode(filePath, StandardCharsets.UTF_8));
        transcript.setAudio_url(upload(filePath));
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);

        String language = mapOfLanguages.get(transcriptionLanguage);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .headers("Authorization", dotenv.get("ASSEMBLY_AI_API_KEY"), "language_code", language == null ? "" : language)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();


        CompletableFuture<HttpResponse<String>> httpResponse = HttpClient.newBuilder()
                .build()
                .sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.get().body());

        transcript = gson.fromJson(httpResponse.get().body(), VideoTranscriptItem.class);

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", dotenv.get("ASSEMBLY_AI_API_KEY"))
                .GET()
                .build();

        while (true) {
            CompletableFuture<HttpResponse<String>> getResponse = HttpClient.newBuilder()
                    .build()
                    .sendAsync(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.get().body(), VideoTranscriptItem.class);
            System.out.println(transcript.getStatus());
            if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
                break;
            }
            Thread.sleep(1000);
        }
        HttpRequest getSrtRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId() + "/srt"))
                .header("Authorization", dotenv.get("ASSEMBLY_AI_API_KEY"))
                .GET()
                .build();

        HttpResponse<String> getSrtResponse = HttpClient.newBuilder()
                .build()
                .send(getSrtRequest, HttpResponse.BodyHandlers.ofString());
        Pattern pattern = Pattern.compile("mp4|mp3|wav|mov");
        Matcher matcher = pattern.matcher(filePath);
        String srtFileName = matcher.replaceAll("srt");
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(srtFileName))) {
            bf.write(getSrtResponse.body().toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
