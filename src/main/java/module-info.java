module com.ensa.videots {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires async.http.client;
    requires java.dotenv;
    requires com.google.gson;
    requires java.desktop;


    opens com.ensa.videots to javafx.fxml;
    requires youtubedl.java;
    requires java.net.http;
    exports com.ensa.videots;
}