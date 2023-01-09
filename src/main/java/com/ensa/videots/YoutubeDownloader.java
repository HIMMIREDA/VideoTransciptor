package com.ensa.videots;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.YoutubeDLRequest;
import com.sapher.youtubedl.YoutubeDLResponse;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDownloader extends Thread {
    private final String videoUrl;
    private final Stage stage;
    private String downloadedVideoFileName = null;
    private final Object monitor;

    public YoutubeDownloader(String videoUrl, Stage stage, Object monitor) {
        this.videoUrl = videoUrl;
        this.stage = stage;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        if (!interrupted()) {

            YoutubeDL.setExecutablePath("src/main/resources/youtube-dl.exe");
            try {
                this.downloadedVideoFileName = download();
                synchronized (monitor){
                    monitor.notify();
                }
            } catch (YoutubeDLException | IOException e) {
                Toast.showToast(stage, e.getMessage(), Toast.TOAST_ERROR);

            }
        }
    }

    public String getDownloadedVideoFileName() {
        return downloadedVideoFileName;
    }

    private String download() throws YoutubeDLException, IOException {

        String directory = Files.readString(Paths.get("src/main/resources/com/ensa/videots/pathToSaveText.txt"));

        YoutubeDLRequest request = new YoutubeDLRequest(videoUrl, directory);


        request.setOption("ignore-errors");
        //si une video appartient a une playlist , download only the video
        request.setOption("no-playlist");
        //Donne titre+ID de la video
        request.setOption("extract-audio");
        request.setOption("output", "%(title)s-%(id)s.mp3");
        request.setOption("audio-format \"mp3\"");
        request.setOption("restrict-filenames");
        request.setOption("no-part");
        request.setOption("no-continue");


        YoutubeDLResponse response = YoutubeDL.execute(request);

        String videoFileName = "";
        Pattern pattern = Pattern.compile("Destination: (.*)\\.mp3");
        Matcher matcher = pattern.matcher(response.getOut());
        if(matcher.find()){
            videoFileName = matcher.group(1);
        }
        return directory + File.separator + videoFileName + ".mp3";
    }


}
