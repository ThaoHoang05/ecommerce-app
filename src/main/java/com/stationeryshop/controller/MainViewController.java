package com.stationeryshop.controller;

import com.stationeryshop.controller.inventory.ProductController;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    private Button logoutBtn;

    @FXML
    private VBox accountVbox;

    @FXML
    private Label accountDropdownArrow;

    @FXML
    private HBox searchHbox;

    @FXML
    private VBox accountDropdown;

    @FXML
    private HBox allProductsTab;

    @FXML
    private HBox categoriesTab;

    @FXML
    private Button categoryBtn3;

    @FXML
    private Button categoryBtn2;

    @FXML
    private Button categoryBtn1;

    @FXML
    private HBox bestSellersTab;

    @FXML
    private AnchorPane mainPane;

    @FXML
    void gotoCartForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("customer".equals(role)) {
            final String CAR_PATH = "/fxml/Cart.fxml";
            Pane cartPane = (new FXMLLoader(getClass().getResource(CAR_PATH))).load();
            AnchorPane.setTopAnchor(cartPane, 0.0);
            AnchorPane.setBottomAnchor(cartPane, 0.0);
            AnchorPane.setLeftAnchor(cartPane, 0.0);
            AnchorPane.setRightAnchor(cartPane, 0.0);
            mainPane.getChildren().addAll(cartPane);
        }
        else{
            JOptionPane.showMessageDialog(null,"You are not a customer");
        }
    
    }

    @FXML
    void gotoHistoryForm(ActionEvent event) {

    }

    @FXML
    void gotoProductForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("admin".equals(role)) {
            final String  PRODUCT_PATH = "/fxml/ProductForm.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(PRODUCT_PATH));
            Pane productPane = loader.load();
            AnchorPane.setTopAnchor(productPane, 0.0);
            AnchorPane.setBottomAnchor(productPane, 0.0);
            AnchorPane.setLeftAnchor(productPane, 0.0);
            AnchorPane.setRightAnchor(productPane, 0.0);
            mainPane.getChildren().addAll(productPane);
        }else{
            JOptionPane.showMessageDialog(null,"You are not an admin","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void gotoSupplierForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("admin".equals(role)) {
            final String  REPORT_PATH = "/fxml/Supplier.fxml";
            BorderPane supplierPane = (new FXMLLoader(getClass().getResource(REPORT_PATH))).load();
            AnchorPane.setTopAnchor(supplierPane, 0.0);
            AnchorPane.setBottomAnchor(supplierPane, 0.0);
            AnchorPane.setLeftAnchor(supplierPane, 0.0);
            AnchorPane.setRightAnchor(supplierPane, 0.0);
            mainPane.getChildren().addAll(supplierPane);

        }else{
            JOptionPane.showMessageDialog(null,"You are not an admin","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void gotoReportForm(MouseEvent event) throws IOException {
        String role = Session.getCurrentRole();
        if("admin".equals(role)) {
            final String  REPORT_PATH = "/fxml/Report.fxml";
            ScrollPane reportPane = (new FXMLLoader(getClass().getResource(REPORT_PATH))).load();
            AnchorPane.setTopAnchor(reportPane, 0.0);
            AnchorPane.setBottomAnchor(reportPane, 0.0);
            AnchorPane.setLeftAnchor(reportPane, 0.0);
            AnchorPane.setRightAnchor(reportPane, 0.0);
            mainPane.getChildren().addAll(reportPane);
        }else{
            JOptionPane.showMessageDialog(null,"You are not an admin","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void gotoHistoryForm(MouseEvent mouseEvent) {
    }

    @FXML
    void gotoAIChatbot(ActionEvent event) {

    }

    @FXML
    void toggleLogoutDropdown(MouseEvent mouseEvent) {
    }

    @FXML
    void gotoShopViewForm(MouseEvent mouseEvent) {
    }

    @FXML
    void toggleCategoriesDropdown(MouseEvent mouseEvent) {
    }

    @FXML
    void handleLogout(ActionEvent actionEvent) {
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
                }else{
                    AccountHbox.setVisible(false);
                    LoginHbox.setVisible(true);
                }
            }
        }

}
