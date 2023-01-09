package com.ensa.videots;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.w3c.dom.ls.LSOutput;

import javax.sound.sampled.LineUnavailableException;

public final class SpeechRecognitionController{

    @FXML
    private Pane pane;
    @FXML
    private Label time;
    @FXML
    private ChoiceBox<String> choice;

    @FXML
    private Button start;

    @FXML
    private Button end;

    @FXML
    private Label errorMsgLabel;

    private Chronometer chrono;

    private Timeline timeline;

    private String[] languages = {"English","French","Spanish"};

    private AudioRecorder audioRecorder = new AudioRecorder();

    private boolean state = true;

    public SpeechRecognitionController() throws LineUnavailableException {
        chrono   = new Chronometer();
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> chrono.update()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }
    @FXML
    private void initialize() {
        time.textProperty().bind(Bindings.format("%02d:%02d:%02d:%d%d", chrono.hh, chrono.mm, chrono.ss, chrono.th, chrono.hd));
        choice.getItems().addAll(languages);
        end.setVisible(false);
    }

    @FXML
    private void start() throws LineUnavailableException {
        errorMsgLabel.setText("");
        if (choice.getValue() != null) {
            timeline.play();
            audioRecorder.begin();
            start.setVisible(false);
            end.setVisible(true);
        }else {
            errorMsgLabel.setText("Please select a language !");
        }
    }
    @FXML
    private void stop() {
        timeline.stop();
        chrono.reset();
        audioRecorder.end();
        start.setVisible(true);
        end.setVisible(false);
        choice.setValue(null);
    }


}
