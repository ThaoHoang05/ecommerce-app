package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestBestSeller extends Application {
    public void start(Stage primaryStage) throws Exception {
        final String BEST_SELLER = "/fxml/BestSellers_Top5.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BEST_SELLER));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Best Seller");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
