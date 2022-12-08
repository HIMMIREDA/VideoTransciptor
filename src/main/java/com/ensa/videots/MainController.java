package com.ensa.videots;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;

public class MainController {
    @FXML
    private StackPane pagesStack;


    public void loadTextToSpeechPage(Pane pane) {

        Form textToSpeechForm = Form.of(
                Group.of(
                        Field.ofSingleSelectionType(Arrays.asList("English", "French", "Spanish", "Arabic"), 1).required("this field cant be empty")
                                .label("Language : ").placeholder("choose language")
                ),
                Group.of(
                        Field.ofSingleSelectionType(Arrays.asList("Jane (female)", "Jhon (male)", "pierre(male)"), 1).required("this field cant be empty")
                                .label("Voice : ").placeholder("choose voice")
                ),
                Group.of(
                        Field.ofStringType("").label("Text : ").multiline(true).required("this field cant be empty").placeholder("text must not surpass 1000 character")
                )
        ).title("textToSpeech");

        pane.getChildren().add(new FormRenderer(textToSpeechForm));
        pane.getStylesheets().add(getClass().getResource("text-to-speech.css").toExternalForm());
    }

    public void loadPage(String fxml) throws IOException {
        Node node = FXMLLoader.load(PageNavigator.class.getResource(fxml));
        switch (fxml) {
            case PageNavigator.TEXTTOSPEECHPAGE:
                loadTextToSpeechPage((Pane) node);
                break;

            default:
                break;

        }
        pagesStack.getChildren().setAll(node);
    }
}
