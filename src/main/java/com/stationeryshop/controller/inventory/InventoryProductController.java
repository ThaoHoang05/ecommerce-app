package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.dao.SupplierDAO;
import com.stationeryshop.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import com.stationeryshop.model.Product;
import com.stationeryshop.model.Category;
import javafx.stage.FileChooser;

import java.io.File;

public class InventoryProductController {

    @FXML
    private TextField productNameField;

    @FXML
    private Button addProductBtn;

    @FXML
    private TextField unitField;

    @FXML
    private Button selectImageButton;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product,String> categoryColumn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private TableColumn<Product,String> stockColumn;

    @FXML
    private Button updateProductBtn;

    @FXML
    private ImageView productImageView;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TextField productIdField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockQuantityField;

    @FXML
    private TableColumn<Product, String> supplierColumn;

    @FXML
    private TableColumn<Product, Float> priceColumn;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    private ObservableList<Product> productList;

    private ProductDAO product;

    private InventoryDAO inventory;

    private SupplierDAO supplier;

    private CategoryDAO category;

    private String imageUrl;

    @FXML
    void handleSortTable(ActionEvent event) {

    }

    @FXML
    void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null); // Mở hộp thoại

        if (file != null) {
            imageUrl = file.toURI().toString();
            Image image = new Image(imageUrl);
            productImageView.setImage(image); // Hiển thị hình ảnh
        } else {
            System.out.println("Không có hình ảnh nào được chọn!");
        }

    }

    @FXML
    void handleAddProduct(ActionEvent event) {
        Category choose = categoryComboBox.getSelectionModel().getSelectedItem();
        product.addProduct(new Product(Integer.parseInt(productIdField.getText()),productNameField.getText(), descriptionArea.getText(),Double.parseDouble(priceField.getText()),imageUrl, choose));
        inventory.updateStock(Integer.parseInt(productIdField.getText()), Integer.parseInt(stockQuantityField.getText()));
        //supplier.addSupplier(new Supplier(Integer.parseInt(productIdField.getText()),unitField.getText()));
        //can update them supplierDAO lai de tim kiem theo ten supplier
    }

    @FXML
    void handleUpdateProduct(ActionEvent event) {
        Category choose = categoryComboBox.getSelectionModel().getSelectedItem();
        product.updateProduct(new Product(Integer.parseInt(productIdField.getText()),productNameField.getText(), descriptionArea.getText(),Double.parseDouble(priceField.getText()),imageUrl, choose));
        inventory.updateStock(Integer.parseInt(productIdField.getText()), Integer.parseInt(stockQuantityField.getText()));
        //update ca nha san xua ma supplier dang thieu
    }

    @FXML
    void handleDeleteProduct(ActionEvent event) {
        product.deleteProduct(Integer.parseInt(productIdField.getText()));
        inventory.deleteStock(Integer.parseInt(productIdField.getText()));
    }
    public void initialize(){
        product = new ProductDAO();
        inventory = new InventoryDAO();
        supplier = new SupplierDAO();
        //set table in the right
        setProductTable();
        ObservableList<Category> cat = FXCollections.observableArrayList(category.getAllCategories());
        categoryComboBox.setItems(cat);
    }

    private void setProductTable(){
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        productList = FXCollections.observableArrayList(product.getAllProductsWithCategory());
        productTable.setItems(productList);
    }

}
