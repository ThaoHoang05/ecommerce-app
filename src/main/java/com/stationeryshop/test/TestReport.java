package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestReport extends Application {
    public void start(Stage primaryStage) throws IOException {
        final String REPORT_PATH = "/fxml/Report.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(REPORT_PATH));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test Report");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
