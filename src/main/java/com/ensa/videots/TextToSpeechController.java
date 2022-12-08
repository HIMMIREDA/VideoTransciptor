package com.ensa.videots;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.HashMap;
import java.util.Map;

public class TextToSpeechController {
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private ComboBox<String> voiceComboBox;

    private static String[] languages = new String[]{"English", "French", "Arabic"};
    private static Map<String, String[]> voices = new HashMap<>();

    static {
        voices.put("English", new String[]{"Linda (female)", "Mary (female)", "Amy (female)", "Mike (male)", "John (male)"});
        voices.put("French", new String[]{"Bette (female)", "Iva (female)", "Zola (female)", "Axel (male)"});
        voices.put("Arabic", new String[]{"oda (female)", "Salim (male)"});
    }

    public void onChangeValue(ActionEvent event) {
        ObservableList<String> voicesOptions = FXCollections.observableArrayList(
                voices.get(languageComboBox.getValue())
        );
        voiceComboBox.setItems(voicesOptions);
        voiceComboBox.setValue(voices.get(languageComboBox.getValue())[0]);

    }

    @FXML
    public void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList(
                languages
        );

        languageComboBox.setItems(options);
        languageComboBox.setValue("English");
        ObservableList<String> voicesOptions = FXCollections.observableArrayList(
                voices.get("English")
        );
        voiceComboBox.setItems(voicesOptions);
        voiceComboBox.setValue(voices.get("English")[0]);

    }

    public void previousPage(ActionEvent event) {
        PageNavigator.loadPage(PageNavigator.TEXTTOSPEECHPAGE);
    }


}
