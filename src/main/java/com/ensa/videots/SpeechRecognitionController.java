package com.ensa.videots;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public final class SpeechRecognitionController {

    @FXML
    private Pane pane;
    @FXML
    private Label time;

    private Chronometer chrono;

    private Timeline    timeline;

    public SpeechRecognitionController() {
        chrono   = new Chronometer();
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> chrono.update()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }
    @FXML
    private void initialize() {
        time.textProperty().bind(Bindings.format("%02d:%02d:%02d:%d%d", chrono.hh, chrono.mm, chrono.ss, chrono.th, chrono.hd));
    }
    @FXML
    private void start() {
        timeline.play();
    }
    @FXML
    private void stop() {
        timeline.stop();
    }

    @FXML
    private void restart() {
        chrono.reset();
    }

    @FXML
    private void exit() {
        stop();
        Platform.exit();
        System.exit(0);
    }

}
