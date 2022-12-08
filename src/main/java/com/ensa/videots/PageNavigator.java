package com.ensa.videots;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class PageNavigator {
    public static final String MAINPAGE = "main.fxml";
    public static final String TEXTTOSPEECHPAGE = "text-to-speech.fxml";

    private static MainController mainController;


    public static void setMainController(MainController mainController) {
        PageNavigator.mainController = mainController;
    }


    public static void loadPage(String fxml) {
        try {
            mainController.loadPage(fxml);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }
}
