package com.stationeryshop.controller;

import com.stationeryshop.Cart_ItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CartControllerSkeletonNew {

    @FXML
    private Label totalLabel;

    @FXML
    private Button checkoutBtn;

    @FXML
    private VBox cartItemsContainer;

    private void loadCartItem( ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/Cart_Item.fxml"));
            HBox cartItemNode = loader.load();

            Cart_ItemController controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private Label itemCountLabel;

    @FXML
    public void initialize() {

    }

    @FXML
    void continueShopping(ActionEvent event) {

    }

    @FXML
    void proceedToCheckout(ActionEvent event) {

    }

    public void updateTotal() {

    }

}

