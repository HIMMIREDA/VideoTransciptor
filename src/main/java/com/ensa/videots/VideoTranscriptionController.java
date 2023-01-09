package com.ensa.videots;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.regex.Pattern;

public class VideoTranscriptionController {

    @FXML
    private TextField link;
    @FXML
    private Label errorLabel;
    public static VideoTranscriptionHandler videoTranscriptionHandlerThread = null;

    public void mousePressed(){
        System.out.println("SETTINGS HAS BEEN CLICKED");
    }

// Upload the video button _____________________________________________________________________________
    public void buttonPressed() {
        // choose file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Videos Files", "*.mp4","*.mp3", "*.wav", "*.mov"));
        // The URL of the video will stored in the selectedFile variable
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) {
            errorLabel.setText("  No file is selected !");
            errorLabel.setTextFill(Paint.valueOf("red"));
            return;
        }

        // stop old thread
        if (videoTranscriptionHandlerThread != null) {
            videoTranscriptionHandlerThread.interrupt();
        }
        Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        // create new thread
        videoTranscriptionHandlerThread = new VideoTranscriptionHandler(null,selectedFile.getPath(),stage,null);
        videoTranscriptionHandlerThread.setDaemon(true);
        videoTranscriptionHandlerThread.start();


    }



//    LINK LABEL AREA__________________________________________________________________________________________

    // get the link passed
    public void submitLink(ActionEvent actionEvent) {
        String text = link.getText();
        if (text.isBlank()) {
            errorLabel.setText("Link cant' be an empty field !");
            errorLabel.setTextFill(Paint.valueOf("red"));
            return;
        }
        if(!Pattern.matches("https://www.youtube.com/watch?v=.*",text)){
            errorLabel.setText("link must be a youtube video link");
            errorLabel.setTextFill(Paint.valueOf("red"));
            return;
        }
        // stop old thread
        if (videoTranscriptionHandlerThread != null) {
            videoTranscriptionHandlerThread.interrupt();
        }
        Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        // create new thread
        videoTranscriptionHandlerThread = new VideoTranscriptionHandler(text,null,stage,null);
        videoTranscriptionHandlerThread.setDaemon(true);
        VideoTranscriptionLoadingController controller = (VideoTranscriptionLoadingController) PageNavigator.loadPage(PageNavigator.VIDEOTRANSCRIPTIONLOADINGPAGE);
        controller.addLoader("step 1/2", "Your video is downloading ...");
        videoTranscriptionHandlerThread.start();
    }


}