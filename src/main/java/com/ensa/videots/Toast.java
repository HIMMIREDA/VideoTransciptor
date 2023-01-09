package com.ensa.videots;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public final class Toast {
    public static final int TOAST_SUCCESS = 11;
    public static final int TOAST_WARN = 12;
    public static final int TOAST_ERROR = 13;

    @FXML
    private HBox containerToast;

    @FXML
    private Label textToast;

    public static void showToast(Stage ownerStage, String toastMsg, int toastType) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Toast.makeToast(toastType, ownerStage, toastMsg);
            }
        });

    }


    private void setToast(int toastType, String content) {
        textToast.setText(content);
        textToast.setWrapText(true);
        switch (toastType) {
            case TOAST_SUCCESS:
                containerToast.setStyle("-fx-background-color: #9FFF96");
                break;
            case TOAST_WARN:
                containerToast.setStyle("-fx-background-color: #FFCF82");
                break;
            case TOAST_ERROR:
                containerToast.setStyle("-fx-background-color: #FF777C");
                break;
        }
    }

    public static void makeToast(int toastType, Stage ownerStage, String text) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setAlwaysOnTop(true);
        double dialogX = dialog.getOwner().getX();
        double dialogY = dialog.getOwner().getY();
        double dialogW = dialog.getOwner().getWidth();
        double dialogH = dialog.getOwner().getHeight();

        double posX = dialogX + dialogW - 208;
        double posY = dialogY + 35;
        dialog.setX(posX);
        dialog.setY(posY);
        dialog.setWidth(200);
        dialog.setHeight(50);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Toast.class.getResource("toastPopup.fxml"));
            loader.load();
            Toast ce = loader.getController();
            ce.setToast(toastType, text);
            dialog.setScene(new Scene(loader.getRoot()));
            dialog.show();
            new Timeline(new KeyFrame(
                    Duration.millis(1500),
                    ae -> {
                        dialog.close();
                    })).play();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}