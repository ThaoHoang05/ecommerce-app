package com.stationeryshop.controller;
import com.stationeryshop.dao.UserDAO;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.FileInputStream;
import java.util.Properties;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Button signupBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button LoginConfirmBtn;

    private Label loginCheck;

    private String useradmin;
    private String pwdadmin;

    public LoginController(){
        Properties props = new Properties();
        try{
            FileInputStream fis = new FileInputStream("src/main/resources/db.properties");
            props.load(fis);
            this.useradmin = props.getProperty("db.loginuser");
            this.pwdadmin = props.getProperty("db.loginpwd");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserDAO dao = new UserDAO(useradmin, pwdadmin);
        if(dao.verifyPassword(username, password)){
            loginCheck.setText("Login Success");
        }else{
            loginCheck.setText("Login Fail");
        }
    }

    @FXML
    void gotoSignupForm(ActionEvent event) {

    }

}

