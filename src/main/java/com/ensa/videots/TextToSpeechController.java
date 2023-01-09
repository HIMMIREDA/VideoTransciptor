package com.ensa.videots;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import org.asynchttpclient.*;

import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TextToSpeechController {
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private ComboBox<String> voiceComboBox;
    @FXML
    private TextArea textToprocessField;
    @FXML
    private Label errorLabel;

    public static AsyncHttpClient client = Dsl.asyncHttpClient();
    private static Map<String, String> languages = new HashMap<>();

    static {
        languages.put("English", "en-us");
        languages.put("French", "fr-fr");
        languages.put("Arabic", "ar-sa");

    }

    private static Map<String, String[]> voices = new HashMap<>();

    static {
        voices.put("English", new String[]{"Linda (female)", "Mary (female)", "Amy (female)", "Mike (male)", "John (male)"});
        voices.put("French", new String[]{"Bette (female)", "Iva (female)", "Zola (female)", "Axel (male)"});
        voices.put("Arabic", new String[]{"oda (female)", "Salim (male)"});
    }

    public void onLanguageChangeValue(ActionEvent event) {
        ObservableList<String> voicesOptions = FXCollections.observableArrayList(
                voices.get(languageComboBox.getValue())
        );
        voiceComboBox.setItems(voicesOptions);
        voiceComboBox.setValue(voices.get(languageComboBox.getValue())[0]);

    }

    @FXML
    public void initialize() {
        Set<String> keys = languages.keySet();
        ObservableList<String> options = FXCollections.observableArrayList(
                keys.toArray(new String[keys.size()])
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

    public void onExportBtnClick(ActionEvent event) {
        String text = textToprocessField.getText().trim();
        String language = languageComboBox.getValue();
        String voice = voiceComboBox.getValue().replaceAll(" \\(male\\)| \\(female\\)", "");

        if (text.isBlank()) {
            errorLabel.setText("Text cant be an empty field !");
            errorLabel.setTextFill(Paint.valueOf("red"));
            return;
        }
        if (text.length() > 1000) {
            errorLabel.setText("Text length cant surpass 1000 character !");
            errorLabel.setTextFill(Paint.valueOf("red"));
            return;
        }

        TextToSpeechReadyController controller = (TextToSpeechReadyController) PageNavigator.loadPage(PageNavigator.TEXTTOSPEECHREADYPAGE);
        controller.addLoader();
        sendTextToSpeechReq(controller, text, language, voice);

    }


    public void sendTextToSpeechReq(TextToSpeechReadyController controller, String text, String language, String voice) {
        Dotenv dotenv = Dotenv.configure().directory("./src/main/java").load();
        BoundRequestBuilder postRequest = client.preparePost("http://api.voicerss.org/?key=" + dotenv.get("VOICE_RSS_API_KEY") + "&r=0&c=mp3&f=16khz_16bit_stereo&hl=" + languages.get(language) + "&v=" + voice + "&src=" + URLEncoder.encode(text, StandardCharsets.UTF_8));
        postRequest.execute(new AsyncCompletionHandler<Object>() {
            @Override
            public Object onCompleted(Response response) throws Exception {
                byte[] arrayOfBytes = response.getResponseBodyAsBytes();
                FileOutputStream fos = new FileOutputStream(Files.readString(Paths.get("src/main/resources/com/ensa/videots/pathToSaveAudio.txt")) + "/" + UUID.randomUUID().toString() + ".mp3");
                fos.write(arrayOfBytes, 0, arrayOfBytes.length);
                fos.flush();
                fos.close();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.stopLoader();
                    }
                });
                return response;
            }
        });


    }


}
