package com.stationeryshop.controller;

import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class MainViewController {
    @FXML
    private VBox onlyForStaff;

    @FXML
    private VBox onlyForCustomer;

    @FXML
    private HBox inventoryTab;

    @FXML
    private ImageView customerAvatar;

    @FXML
    private Button loginBtn;

    @FXML
    private StackPane ContentArea;

    @FXML
    private TextField searchitemField;

    @FXML
    private Label customerName;

    @FXML
    private Button signupBtn;

    @FXML
    private HBox reportTab;

    @FXML
    private HBox AccountHbox;


    @FXML
    private Region spacer;

    @FXML

    private HBox supplierTab;

    @FXML
    private Button AIChatbotBtn;

    @FXML

    private HBox cartTab;

    @FXML
    private HBox LoginHbox;
  
  
    @FXML
    void gotoCartForm(ActionEvent event) {
    
    }


    @FXML
    void gotoProductForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("admin".equals(role)) {
            final String  PRODUCT_PATH = "/fxml/ProductForm.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PRODUCT_PATH));
            Parent root = loader.load();
            Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
            ProductController controller = loader.getController();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }else{
            JOptionPane.showMessageDialog(null,"You are not an admin","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void gotoSupplierForm(ActionEvent event) {
      
    }

    @FXML
    void gotoReportForm(ActionEvent event) {

    }

    @FXML
    void gotoAIChatbot(ActionEvent event) {

    }

    @FXML
    void gotoLoginForm(ActionEvent event) {

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

    @FXML
    public void initialize(){
            if(Session.isLoggedIn()){
                String role = Session.getCurrentRole();
                if("admin".equals(role)){
                    onlyForCustomer.setManaged(false);
                    onlyForCustomer.setVisible(false);

                }else if("customer".equals(role)){
                    onlyForStaff.setManaged(false);
                    onlyForStaff.setVisible(false);
                }
                if(role != null){
                    customerName.setText(Session.getCurrentUsername());
                    AccountHbox.setVisible(true);
                    LoginHbox.setVisible(false);
                }
            }
        }
}
