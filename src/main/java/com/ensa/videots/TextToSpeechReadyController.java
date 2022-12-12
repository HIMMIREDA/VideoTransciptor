package com.ensa.videots;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TextToSpeechReadyController {
    @FXML
    private ImageView loaderImageView;

    @FXML
    private Label readyLabel;


    public void addLoader(){
        loaderImageView.setImage(new Image(getClass().getResourceAsStream("icons/loader.gif")));
        readyLabel.setText("Processing Text , please wait ...");
    }
    public void stopLoader(){
        loaderImageView.setImage(new Image(getClass().getResourceAsStream("icons/smiley_face.png")));
        readyLabel.setText("Your .mp3 file is ready !");
    }
}
