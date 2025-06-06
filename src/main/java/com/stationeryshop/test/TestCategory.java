package com.stationeryshop.test;

import com.stationeryshop.controller.CategoriesController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TestCategory extends Application {
    public void start(Stage primaryStage) throws Exception {
        final String   CATEGORY = "/fxml/Categories.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CATEGORY));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Test Category");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
