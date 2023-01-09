package com.ensa.videots;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class VideoTranscriptionLoadingController {
    @FXML
    private ImageView loaderImageView;
    @FXML
    private Text loaderText;
    @FXML
    private Text stepText;


    public void addLoader(String stepText,String loaderText){
        loaderImageView.setImage(new Image(getClass().getResourceAsStream("icons/loader.gif")));
        this.loaderText.setText(loaderText);
        this.stepText.setText(stepText);
    }
    public void stopLoader(){
        loaderImageView.setImage(new Image(getClass().getResourceAsStream("icons/smiley_face.png")));
        loaderText.setText("Your .srt file is ready !");
    }
}
