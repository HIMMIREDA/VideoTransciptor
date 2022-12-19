package com.ensa.videots;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.asynchttpclient.*;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BrowseYoutubeController {

    @FXML
    private TilePane videoItemsTilePane;
    public static AsyncHttpClient client = Dsl.asyncHttpClient();


    public void initialize() {

        videoItemsTilePane.setHgap(10);
        videoItemsTilePane.setVgap(10);
        getVideoItems();
    }

    private void getVideoItems() {
        Dotenv dotenv = Dotenv.configure().directory("./src/main/java").load();
        //BoundRequestBuilder getRequest = client.prepareGet("https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=surfing&type=video&key=" + dotenv.get("YOUTUBE_API_KEY"));
        BoundRequestBuilder getRequest = client.prepareGet(
                "https://youtube.googleapis.com/youtube/v3/search?chart=mostPopular&regionCode=MA&maxResults=25&type=video&key="+dotenv.get("YOUTUBE_API_KEY")+"&part=snippet&order=date"
        );


        getRequest.execute(new AsyncCompletionHandler<Object>() {
            @Override
            public Object onCompleted(Response response) throws Exception {
                String items = response.getResponseBody();
                VideoItems videoItems = new Gson().fromJson(items, VideoItems.class);

                List<VBox> videoItemsXML = Arrays.stream(videoItems.items).map(videoItem -> {
                    VBox vBox = new VBox();
                    vBox.setPrefHeight(300);
                    vBox.setPrefWidth(270);
                    Label titleLabel = new Label(videoItem.snippet.title);
                    ImageView thumbnail = new ImageView(videoItem.snippet.thumbnails.medium.url);
                    thumbnail.setFitHeight(150);
                    thumbnail.setFitWidth(260);
                    titleLabel.setTextFill(Paint.valueOf("#fff"));
                    vBox.setAlignment(Pos.TOP_CENTER);
                    vBox.setPadding(new Insets(5,5,5,5));
                    vBox.getChildren().add(titleLabel);
                    vBox.getChildren().add(thumbnail);
                    vBox.setStyle("-fx-background-color: #000;-fx-background-radius: 10px");
                    return vBox;
                }).collect(Collectors.toList());


                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadVideoItems(videoItemsXML);
                    }
                });
                return response;
            }
        });


    }

    private void loadVideoItems(List<VBox> videoItemsXML) {
        for (VBox vBox:
                videoItemsXML) {
            videoItemsTilePane.getChildren().add( vBox);
        }

        videoItemsTilePane.getChildren().add(new Pagination());
    }
}
