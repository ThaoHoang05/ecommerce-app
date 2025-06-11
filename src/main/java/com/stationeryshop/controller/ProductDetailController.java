package com.stationeryshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;

public class ProductDetailController {

    @FXML
    private Label categoryLbl;

    @FXML
    private Button addToCartBtn;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productNameLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label quantityLbl;

    @FXML
    private Label productIdLbl;

    @FXML
    private Button backBtn;

    @FXML
    private Label supplierLbl;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    void handleGoBack(ActionEvent event) {

    }

    @FXML
    void handleAddToCart(ActionEvent event) {

    }

}

