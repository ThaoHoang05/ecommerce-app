package com;

import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class MainViewController {

    @FXML
    private Button signupBtn;

    @FXML
    private ImageView customerAvatar;

    @FXML
    private Button loginBtn;

    @FXML
    private HBox AccountHbox;

    @FXML
    private StackPane ContentArea;

    @FXML
    private Region spacer;

    @FXML
    private TextField searchitemField;

    @FXML
    private Button AIChatbotBtn;

    @FXML
    private HBox LoginHbox;

    @FXML
    private Label customerName;


    @FXML
    void gotoAIChatbot(ActionEvent event) {

    }

    @FXML
    void gotoLoginForm(ActionEvent event) {
    }


    @FXML
    void gotoSignupForm(ActionEvent event) {
    }

    public MainViewController(){

    }
}
