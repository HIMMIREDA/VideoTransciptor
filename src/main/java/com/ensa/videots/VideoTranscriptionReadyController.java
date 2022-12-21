package com.ensa.videots;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class VideoTranscriptionReadyController {

    @FXML
    private ImageView loaderImageView;

    @FXML
    private Text readyText;
    @FXML
    private Button exportButton;
    @FXML
    private ImageView downIcon;

    // BUTTON DOWNLOAD TEXT
    public void pressedButton(MouseEvent mouseEvent) {
        // START DOWNLOADING THE TEXT FILE FROM LINK
    }

    public void addLoader(){
        loaderImageView.setImage(new Image(getClass().getResourceAsStream("icons/loader.gif")));
        readyText.setText("Processing, please wait...");
        exportButton.setVisible(false);
        downIcon.setVisible(false);
    }
    public void stopLoader(){
        loaderImageView.setImage(new Image(getClass().getResourceAsStream("icons/smiley_face.png")));
        readyText.setText("Your text file is ready !");
        exportButton.setVisible(true);
        downIcon.setVisible(true);

    }
}
