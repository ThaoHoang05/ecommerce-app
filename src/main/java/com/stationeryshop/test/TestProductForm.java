package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;

public class TestProductForm extends Application {

    public void start(Stage primaryStage) throws IOException {
        final String PRODUCT_PATH = "/fxml/Inventory/ProductForm.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PRODUCT_PATH));
        long startTime = System.nanoTime();

        Parent root = fxmlLoader.load();
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime)/1_000_000;
        System.out.println("Elapsed time: " + elapsedTime);
        primaryStage.setTitle("Product Form");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {

        launch(args);

    }
}
