package com.ensa.videots;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class TextToSpeechController {

    public void previousPage(ActionEvent event){
        PageNavigator.loadPage(PageNavigator.TEXTTOSPEECHPAGE);
    }


}
