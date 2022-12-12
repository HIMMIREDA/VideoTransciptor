package com.ensa.videots;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(PageNavigator.MAINPAGE));
        MainController mainController = loader.getController();
        PageNavigator.setMainController(mainController);
        PageNavigator.loadPage(PageNavigator.TEXTTOSPEECHPAGE);
        return mainPane;
    }

    public Scene createScene(Pane mainPane) {
        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        return scene;
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("video transcriptor");
        stage.setScene(createScene(loadMainPane()));
        stage.setMinWidth(720.0);
        stage.setMaxWidth(720.0);
        stage.setMinHeight(512.0);
        stage.setMaxHeight(512.0);
        stage.setOnCloseRequest(event -> {
            try {
                TextToSpeechController.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
