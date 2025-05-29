package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestMainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        primaryStage.setScene(new Scene(root, 1315, 800));
        primaryStage.setTitle("Stationery");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

