package com.stationeryshop.controller;

import javax.swing.JOptionPane;

import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        int result = new UserController().handleLogin(usernameField.getText(), passwordField.getText());
        if(result == 1){ 
            JOptionPane.showMessageDialog(null, "Login Successful");
            User cur = new UserDAO().getUser(usernameField.getText());
            System.out.println(cur.getUsername());
        }
        else if(result == 0) JOptionPane.showMessageDialog(null, "Wrong Password","ERROR",JOptionPane.ERROR_MESSAGE);
        else JOptionPane.showMessageDialog(null, "User Not Found","ERROR",JOptionPane.ERROR_MESSAGE);
    }

    @FXML
    void gotoSignupForm(ActionEvent event) {

    }

}
