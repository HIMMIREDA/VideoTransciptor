package com.ensa.videots;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
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
    private final String previousInterface;

    static {
        mapOfLanguages.put("English", "en");
        mapOfLanguages.put("Spanish", "es");
        mapOfLanguages.put("French", "fr");
    }


    public VideoTranscriptionHandler(String videoUrl, String filePath, Stage stage, String language,String previousInterface) {
        if (videoUrl != null) {
            this.youtubeDownloaderThread = new YoutubeDownloader(videoUrl, stage, this,previousInterface);
            youtubeDownloaderThread.setDaemon(true);
        }
        this.filePath = filePath;
        this.stage = stage;
        this.transcriptionLanguage = language;
        this.previousInterface = previousInterface;
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
                Toast.showToast(stage, e.getMessage() == null ? "sorry an error occured !" : e.getMessage(), Toast.TOAST_ERROR);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        PageNavigator.loadPage(previousInterface);
                    }
                });
                return;
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
        transcript.setAudio_url(upload(filePath));
        String languageCode = mapOfLanguages.get(transcriptionLanguage);
        transcript.setLanguage_code(languageCode);
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .headers("Authorization", dotenv.get("ASSEMBLY_AI_API_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();


        CompletableFuture<HttpResponse<String>> httpResponse = HttpClient.newBuilder()
                .build()
                .sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());


        transcript = gson.fromJson(httpResponse.get().body(), VideoTranscriptItem.class);

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .headers("Authorization", dotenv.get("ASSEMBLY_AI_API_KEY"))
                .GET()
                .build();

        while (true) {
            CompletableFuture<HttpResponse<String>> getResponse = HttpClient.newBuilder()
                    .build()
                    .sendAsync(getRequest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.get().body(), VideoTranscriptItem.class);
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
        String slashPattern = Pattern.quote(System.getProperty("file.separator"));
        Pattern pattern = Pattern.compile("([^/\\\\]+(\\.(?i)(mp4|mp3|wav|mov?))$)");
        Matcher matcher = pattern.matcher(filePath);
        String srtFileName = "";
        if (matcher.find()) {
            srtFileName = matcher.group(1);
        }
        srtFileName = srtFileName.replaceAll("\\.mp4|\\.mp3|\\.wav|\\.mov","");
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(Files.readString(Paths.get("src/main/resources/com/ensa/videots/pathToSaveText.txt")) + File.separator + srtFileName + ".srt"))) {
            bf.write(getSrtResponse.body().toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
