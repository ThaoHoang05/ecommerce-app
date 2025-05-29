package com.stationeryshop.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class TestMainView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
      
        try{
        final String MAINVIEW_PATH = "/fxml/MainView.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent root = loader.load();
        primaryStage.setTitle("Stationery Shop");
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

