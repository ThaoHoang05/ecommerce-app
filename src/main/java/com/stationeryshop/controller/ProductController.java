package com.stationeryshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.Category;
import com.stationeryshop.model.InventoryItem;
import com.stationeryshop.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductController {

    @FXML
    private Button addProductBtn;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField unitField;

    @FXML
    private TextField searchField;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button searchBtn;

    @FXML
    private TableView<?> productTable;

    @FXML
    private TableColumn<?, ?> categoryColumn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private TableColumn<?, ?> stockColumn;

    @FXML
    private Button updateProductBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private ImageView productImageView;

    @FXML
    private ComboBox<?> categoryComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextField productIdField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockQuantityField;

    @FXML
    private TableColumn<?, ?> supplierColumn;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    void handleAddProduct(ActionEvent event) {

    }

    @FXML
    void handleSortTable(ActionEvent event) {

    }

    @FXML
    void handleSelectImage(ActionEvent event) {

    }

    @FXML
    void handleUpdateProduct(ActionEvent event) {

    }

    @FXML
    void handleDeleteProduct(ActionEvent event) {

    }

    @FXML
    void handleSearch(ActionEvent event) {

    }

    @FXML
    void handleRefresh(ActionEvent event) {

    }

}