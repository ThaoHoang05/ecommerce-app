package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestProductForm extends Application {
    public void start(Stage primaryStage) throws IOException {
        final String PRODUCT_PATH = "/fxml/ProductForm.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PRODUCT_PATH));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Product Form");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
