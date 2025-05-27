package com.stationeryshop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;

public class LoginFormController {

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
        UserController currentUser = new UserController();
        int canLogin = currentUser.handleLogin(usernameField.getText(), passwordField.getText());
        switch(canLogin){
            case 0:
                System.out.println("Invalid username or password");
                JOptionPane.showMessageDialog(null, "Invalid username or password","Login Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
                break;
            case 1:
                System.out.println("Login successful");
                //Chuyen toi mainview theo vai tro nguoi dung
            break;
            default:
                JOptionPane.showMessageDialog(null, "Something went wrong","Login Error", JOptionPane.ERROR_MESSAGE);
                // Thoat man hinh login

                System.out.println("Something went wrong");
        }
    }

    @FXML
    void gotoSignupForm(ActionEvent event) {

    }

}
