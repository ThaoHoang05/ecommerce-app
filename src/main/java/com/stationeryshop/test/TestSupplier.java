package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestSupplier extends Application {
    public void start(Stage primaryStage) throws IOException {
        final String SUPPLIER_PATH = "/fxml/Supplier.fxml";
        Parent root = (new FXMLLoader(getClass().getResource(SUPPLIER_PATH)).load());
        Scene scene = new Scene(root);
        primaryStage.setTitle("Supplier");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
