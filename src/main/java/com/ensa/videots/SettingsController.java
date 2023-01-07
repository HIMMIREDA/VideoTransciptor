package com.ensa.videots;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsController {

    @FXML
    public Label pathName;
    @FXML
    public Label pathName1;
    public static File selectedDirectory;
    public static File selectedDirectory1;
    Path path = Paths.get("src/main/resources/com/ensa/videots/pathToSaveAudio.txt");
    Path path1 = Paths.get("src/main/resources/com/ensa/videots/pathToSaveText.txt");


    public void initialize() throws IOException {
        if (selectedDirectory == null) {
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/");
            if (pathName != null) {
                pathName.setText(String.valueOf(file));
            }
        } else {
            pathName.setText(selectedDirectory.getAbsolutePath());
        }

        // for the text path location
        if (selectedDirectory1 == null) {
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/");
            if (pathName1 != null) {
                pathName1.setText(String.valueOf(file));
            }
        } else {
            pathName1.setText(selectedDirectory1.getAbsolutePath());
        }

        // get the path where to stock the Audio file(path)
        try {

            pathName.setText(Files.readString(path));
        } catch (IOException ex) {
            File file = new File(path.toString());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(pathName.getText().getBytes());
        }
        try {

            pathName1.setText(Files.readString(path1));
        } catch (IOException ex) {
            File file = new File(path1.toString());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(pathName1.getText().getBytes());
        }

    }


    public void changePressed(MouseEvent mouseEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            return;
        }
        pathName.setText(selectedDirectory.getAbsolutePath());

        try {
            // Now calling Files.writeString() methode with path , content & standard charsets
            Files.writeString(path, pathName.getText(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.print("Invalid Path");
        }
    }

    // for the second Location (text)
    public void changePressed1(MouseEvent mouseEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory1 = directoryChooser.showDialog(null);
        if (selectedDirectory1 == null) {
            return;
        }
        pathName1.setText(selectedDirectory1.getAbsolutePath());

        try {
            // Now calling Files.writeString() methode with path , content & standard charsets
            Files.writeString(path1, pathName1.getText(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.print("Invalid Path");
        }
    }
}
