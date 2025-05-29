package com.stationeryshop.controller;

import javax.swing.JOptionPane;

import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
        try{
            final String SIGNUP_PATH = "/fxml/Signup.fxml";
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(SIGNUP_PATH));
            Parent root = fxmlloader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Signup");
            stage.show();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
