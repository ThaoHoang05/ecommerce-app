package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestSignup extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String SIGNUP_FILE_PATH = "/fxml/Signup.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SIGNUP_FILE_PATH));
        Parent root = loader.load();
        primaryStage.setTitle("Signup");
        primaryStage.setScene(new Scene(root, 550, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
