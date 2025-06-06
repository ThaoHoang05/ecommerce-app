package com.stationeryshop.controller;


import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShopView_ItemController {

    @FXML
    private Label categoryLbl;

    @FXML
    private Button addToCartButton;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productNameLbl;

    @FXML
    private Label productPriceLbl;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Label productSupplierLbl;

    @FXML
    private Label productQuantityLabel;

    InventoryProduct product ;

    public ShopView_ItemController(InventoryProduct product) {
        this.product = product;
    }

    @FXML
    void addToCartOnPressed(ActionEvent event) {
        int id = product.getProductId();
        int quantity = product.getQuantity();

    }

    @FXML
    void viewDetailsOnPressed(ActionEvent event) {
    }
    public void setData(){
        categoryLbl.setText(product.getCategoryName());
        productNameLbl.setText(product.getProductName());
        productPriceLbl.setText(String.valueOf(product.getProductPrice()));
        productSupplierLbl.setText(product.getSupplierName());
        productImage.setImage(new Image(product.getImageUrl()));
        if(product.getQuantity() == 0 ) addToCartButton.setDisable(true);
    }
}
