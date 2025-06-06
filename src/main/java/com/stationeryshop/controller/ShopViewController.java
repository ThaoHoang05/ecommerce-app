package com.stationeryshop.controller;

import com.stationeryshop.model.InventoryProduct;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class ShopViewController {

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<?> sortComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane productsScrollPane;

    @FXML
    private FlowPane productsFlowPane;
    public void initialize(){

    }
}
