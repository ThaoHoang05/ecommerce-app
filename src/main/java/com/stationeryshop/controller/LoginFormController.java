package com.stationeryshop.controller;

import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class LoginFormController extends Application {

    @FXML
    private TextField usernameField;

    @FXML
    private Button signupBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button LoginConfirmBtn;

    @FXML
    void handleLogin(ActionEvent event) {
        int result = new UserController().handleLogin(usernameField.getText(), passwordField.getText());
        if(result == 1) {
            JOptionPane.showMessageDialog(null, "Login Successful");
            //Tao doi tuong nguoi dung, tao session roi tien hanh chuyen sang mainview
            Session.setCurrentUser(new UserDAO().getUser(usernameField.getText()));
            // Nhay sang mainview
            launch();
        }
        else if(result == 0) JOptionPane.showMessageDialog(null, "Wrong Password","ERROR",JOptionPane.ERROR_MESSAGE);
        else JOptionPane.showMessageDialog(null, "User Not Found","ERROR",JOptionPane.ERROR_MESSAGE);
    }

    @FXML
    void gotoSignupForm(ActionEvent event) {

    }
    public void start(Stage primaryStage) throws IOException {
        final String LOGIN_PATH = "/fxml/Login.fxml";
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(LOGIN_PATH));
        fxmlloader.setController(this);
        Parent root = fxmlloader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
