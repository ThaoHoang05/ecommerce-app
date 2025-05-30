package com.stationeryshop.controller;

import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class SignupFormController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private CheckBox acceptTermsCheckBox;

    @FXML
    private Button SignupConfirmBtn;

    @FXML
    void handleSignup(ActionEvent event) {
        int result = new UserController().handleSignup(usernameField.getText());
        switch (result){
            case 1: System.out.println("Signup Successful");
            //Thoat ra tro ve mainview voi session phu hop voi role nguoi dung
                Session.setCurrentUser( new UserDAO().createUser(usernameField.getText(), passwordField.getText(),nameField.getText(),emailField.getText()));
                //Chuyen den mainview
                this.returnMainView(event);
                break;
            case 0: JOptionPane.showMessageDialog(null, "User name exists!","ERROR",JOptionPane.ERROR_MESSAGE);
                usernameField.clear();
            break;
            case -1:
                JOptionPane.showMessageDialog(null, "Something went wrong", "ERROR", JOptionPane.ERROR_MESSAGE);
                // Thoat ra tro ve mainview
                this.returnMainView(event);
                break;
        }
    }
    @FXML
    void gotoLoginForm(ActionEvent event) {
        //Chuyen toi login form
        try{
            final String    LOGIN_PATH = "/fxml/Login.fxml";
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(LOGIN_PATH));
            Parent root = fxmlloader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    void returnMainView(ActionEvent event) {
        try{
            final String MAINVIEW_PATH = "/fxml/MainView.fxml";
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
            Parent root = fxmlloader.load();
            MainViewController controller = fxmlloader.getController();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Stationery Shop");
            stage.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
