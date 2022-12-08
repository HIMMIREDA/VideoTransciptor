package com.ensa.videots;

import java.io.IOException;

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
