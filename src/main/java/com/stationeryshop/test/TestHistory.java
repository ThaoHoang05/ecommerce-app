package com.stationeryshop.test;

import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestHistory extends Application{
    public void start(Stage primaryStage) throws Exception {
        final String LOGIN_FXML_PATH = "/fxml/History.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_FXML_PATH));
        Parent root = loader.load();
        primaryStage.setTitle("History");
        primaryStage.setScene(new Scene(root, 550, 800));
        primaryStage.show();
    }
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        User cur = userDAO.getUser("customer2");
        Session.setCurrentUser(cur);
        launch(args);
    }
}
