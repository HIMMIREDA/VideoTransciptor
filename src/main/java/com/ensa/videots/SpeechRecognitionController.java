package com.ensa.videots;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;

public final class SpeechRecognitionController{

    @FXML
    private Pane pane;
    @FXML
    private Label time;
    @FXML
    private ChoiceBox<String> choice;

    private Chronometer chrono;

    private Timeline timeline;

    private String[] languages = {"English","French","Spanish","Arabic"};

    private AudioRecorder audioRecorder = new AudioRecorder();

    public SpeechRecognitionController() throws LineUnavailableException {
        chrono   = new Chronometer();
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> chrono.update()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }
    @FXML
    private void initialize() {
        time.textProperty().bind(Bindings.format("%02d:%02d:%02d:%d%d", chrono.hh, chrono.mm, chrono.ss, chrono.th, chrono.hd));
        choice.getItems().addAll(languages);

    }
    @FXML
    private void start() throws LineUnavailableException {
        timeline.play();
        audioRecorder.begin();
    }
    @FXML
    private void stop() {
        timeline.stop();
        chrono.reset();
        audioRecorder.end();
    }

}
