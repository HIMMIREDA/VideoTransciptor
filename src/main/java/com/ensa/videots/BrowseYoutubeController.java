package com.ensa.videots;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.asynchttpclient.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BrowseYoutubeController {

    @FXML
    private TilePane videoItemsTilePane;
    @FXML

    private Button loadItemsBtn;
    @FXML
    private TextField searchTextField;
    public static AsyncHttpClient client = Dsl.asyncHttpClient();

    public static VideoTranscriptionHandler videoTranscriptionHandlerThread = null;


    public void initialize() {

        videoItemsTilePane.setHgap(10);
        videoItemsTilePane.setVgap(10);
        getVideoItems(null);
    }

    private void getVideoItems(String pageToken) {
        Dotenv dotenv = Dotenv.configure().directory("./src/main/java").load();
        loadItemsBtn.setVisible(false);
        searchTextField.setVisible(false);
        BoundRequestBuilder getRequest = client.prepareGet(
                "https://youtube.googleapis.com/youtube/v3/search?chart=mostPopular&regionCode=MA&maxResults=26&type=video&key=" + dotenv.get("YOUTUBE_API_KEY") + "&part=snippet&order=date" + (pageToken == null ? "" : "&pageToken=" + pageToken) + (searchTextField.getText().isBlank() ? "" : "&q=" + searchTextField.getText().trim())
        );

        getRequest.execute(new AsyncCompletionHandler<Object>() {
            @Override
            public void onThrowable(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public Object onCompleted(Response response) throws Exception {
                int statusCode = response.getStatusCode();
                if (200 > statusCode || statusCode >= 300) {
                    Toast.showToast((Stage) searchTextField.getScene().getWindow(), "Sorry an error has occured !", Toast.TOAST_ERROR);
                    return null;
                }
                String items = response.getResponseBody();
                VideoItems videoItems = new Gson().fromJson(items, VideoItems.class);

                List<VBox> videoItemsXML = Arrays.stream(videoItems.items).map(videoItem -> {
                    VBox vBox = new VBox();
                    vBox.setPrefHeight(220);
                    vBox.setPrefWidth(270);
                    // video label
                    Label titleLabel = new Label(videoItem.snippet.title);
                    titleLabel.setTextFill(Paint.valueOf("#fff"));

                    // video thumbnail
                    ImageView thumbnail = new ImageView(videoItem.snippet.thumbnails.mediumImage.url);
                    thumbnail.setFitHeight(150);
                    thumbnail.setFitWidth(260);

                    // video infos


                    // download button
                    Button button = new Button();
                    button.setText("Download");
                    button.setTextFill(Paint.valueOf("white"));
                    button.setBackground(Background.fill(Paint.valueOf("green")));
                    button.setStyle("-fx-cursor: hand");
                    button.setOnMouseExited(e -> button.setStyle("-fx-background-color: 'green';-fx-cursor: hand;"));
                    button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color;-fx-text-fill: #000;-fx-cursor: hand;"));
                    button.setGraphic(new ImageView(getClass().getResource("icons/thunder.png").toExternalForm()));
                    button.setContentDisplay(ContentDisplay.RIGHT);

                    // download button listener
                    button.setOnAction(event -> {
                        // stop old thread
                        if (videoTranscriptionHandlerThread != null) {
                            videoTranscriptionHandlerThread.interrupt();
                        }
                        Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
                        // create new thread
                        videoTranscriptionHandlerThread = new VideoTranscriptionHandler("https://www.youtube.com/watch?v=" + videoItem.id.videoId,null,stage,null);
                        videoTranscriptionHandlerThread.setDaemon(true);
                        VideoTranscriptionLoadingController controller = (VideoTranscriptionLoadingController) PageNavigator.loadPage(PageNavigator.VIDEOTRANSCRIPTIONLOADINGPAGE);
                        controller.addLoader("step 1/2", "Your video is downloading ...");
                        videoTranscriptionHandlerThread.start();
                    });

                    vBox.setAlignment(Pos.TOP_CENTER);
                    vBox.setPadding(new Insets(5, 5, 5, 5));
                    vBox.setSpacing(10);
                    vBox.getChildren().add(thumbnail);
                    vBox.getChildren().add(titleLabel);
                    vBox.getChildren().add(button);
                    vBox.setStyle("-fx-background-color: #000;-fx-background-radius: 10px");
                    return vBox;
                }).collect(Collectors.toList());


                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadVideoItems(videoItemsXML, videoItems.prevPageToken, videoItems.nextPageToken);
                    }
                });
                return response;
            }
        });


    }

    private void loadVideoItems(List<VBox> videoItemsXML, String prevPageToken, String nextPageToken) {
        for (VBox vBox :
                videoItemsXML) {
            videoItemsTilePane.getChildren().add(vBox);
        }

        loadItemsBtn.setVisible(true);
        searchTextField.setVisible(true);


        if (nextPageToken == null) {
            loadItemsBtn.setDisable(true);
        } else {
            loadItemsBtn.setOnAction(e -> {
                getVideoItems(nextPageToken);
            });
        }


    }

    public void SearchVideos(ActionEvent event) {
        if (!searchTextField.getText().isBlank()) {
            videoItemsTilePane.getChildren().clear();
            getVideoItems(null);
        }
    }
}
