package com.ensa.videots;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import java.io.File;

public class VideoTranscriptionController {

    @FXML
    private TextField link;
    @FXML
    private Label errorLabel;

    public void mousePressed(){
        System.out.println("SETTINGS HAS BEEN CLICKED");
    }

// Upload the video button _____________________________________________________________________________
    public void buttonPressed() {
        // choose file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Videos Files", "*.mp4"));
        // The URL of the video will stored in the selectedFile variable
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) {
            errorLabel.setText("  No Video file is selected !");
            errorLabel.setTextFill(Paint.valueOf("red"));
            return;
        }

        // Go to the ready page
        VideoTranscriptionReadyController controller = (VideoTranscriptionReadyController) PageNavigator.loadPage(PageNavigator.VIDEOTRANSCRIPTIONREADYPAGE);
        controller.addLoader();
        videoToTextReq(controller, selectedFile);
        controller.stopLoader();

    }

    private void videoToTextReq(VideoTranscriptionReadyController controller, File selectedFile) {
        // vid to text ALGORITHME HERE
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
        // Go to the ready page
        VideoTranscriptionReadyController controller = (VideoTranscriptionReadyController) PageNavigator.loadPage(PageNavigator.VIDEOTRANSCRIPTIONREADYPAGE);
        controller.addLoader();
        videoLinkToTextReq(controller, text);
//        controller.stopLoader();
    }

    private void videoLinkToTextReq(VideoTranscriptionReadyController controller, String text) {
        //link vidToText ALGORITHME here
    }

}