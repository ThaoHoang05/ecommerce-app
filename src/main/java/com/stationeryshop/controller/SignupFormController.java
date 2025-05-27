package com.stationeryshop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;

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
        int result = new UserController().handleSignup(usernameField.getText(),passwordField.getText(),nameField.getText() ,emailField.getText());
        switch (result){
            case 1: System.out.println("Signup Successful");
            //Thoat ra tro ve mainview voi session phu hop voi role nguoi dung
            break;
            case 0: JOptionPane.showMessageDialog(null, "User name exists!","ERROR",JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
            break;
            case -1:
                JOptionPane.showMessageDialog(null, "Something went wrong", "ERROR", JOptionPane.ERROR_MESSAGE);
                // Thoat ra tro ve mainview
                break;
        }
    }
    @FXML
    void gotoLoginForm(ActionEvent event) {
        //Chuyen toi login form
    }
}
