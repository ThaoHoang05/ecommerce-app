package com.stationeryshop.view.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestSignup extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Signup.fxml"));
        primaryStage.setTitle("Signup");
        primaryStage.setScene(new Scene(root, 115, 735));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
