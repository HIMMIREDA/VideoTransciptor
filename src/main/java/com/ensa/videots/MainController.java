package com.ensa.videots;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {
    @FXML
    private StackPane pagesStack;


    public void loadTextToSpeechPage(Pane pane) {
        pane.getStylesheets().add(getClass().getResource("text-to-speech.css").toExternalForm());
    }

    public void loadPage(String fxml) throws IOException {
        Node node = FXMLLoader.load(PageNavigator.class.getResource(fxml));
        if (PageNavigator.TEXTTOSPEECHPAGE.equals(fxml)) {
            loadTextToSpeechPage((Pane) node);
        }
        pagesStack.getChildren().setAll(node);
    }
}
