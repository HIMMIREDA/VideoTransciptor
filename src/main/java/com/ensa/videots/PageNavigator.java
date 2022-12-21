package com.ensa.videots;

import java.io.IOException;
import java.util.Objects;

public class PageNavigator {
    public static final String MAINPAGE = "main.fxml";
    public static final String TEXTTOSPEECHPAGE = "text-to-speech.fxml";
    public static final String TEXTTOSPEECHREADYPAGE = "text-to-speech-ready.fxml";
    public static final String SETTINGSPAGE = "settings.fxml";
    public static final String SPEECHRECOGNITIONPAGE = "speech-recognition.fxml";
    public static final String VIDEOTRANSCRIPTIONPAGE = "video-transcription.fxml";
    public static final String INFOSPAGE = "infos.fxml";
    public static final String VIDEOTRANSCRIPTIONREADYPAGE = "video-transcription-ready.fxml";


    private static MainController mainController;


    public static void setMainController(MainController mainController) {
        PageNavigator.mainController = mainController;
    }


    public static Object loadPage(String fxml) {
        Object pageController = null;
        try {
            pageController = mainController.loadPage(fxml);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
        return pageController;
    }
}
