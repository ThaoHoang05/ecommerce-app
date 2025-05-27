package com.stationeryshop.controller;
import com.stationeryshop.dao.UserDAO;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;

import java.io.FileInputStream;
import java.util.Properties;

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
        if (acceptTermsCheckBox.isSelected()) {
            // Đồng ý → xử lý đăng ký
        } else {
            // Chưa đồng ý → cảnh báo
        }


    }

    @FXML
    void gotoLoginForm(ActionEvent event) {

    }
}

