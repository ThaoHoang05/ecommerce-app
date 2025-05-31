package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.dao.SupplierDAO;
import com.stationeryshop.model.InventoryItem;
import com.stationeryshop.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.util.Callback;

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
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private TableColumn<Product, Integer> stockColumn; // Fixed: Changed from InventoryItem to Product

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
    private TableColumn<Product, Double> priceColumn; // Fixed: Changed from Float to Double

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    private ObservableList<Product> productList;

    private ProductDAO product = new ProductDAO();
    private InventoryDAO inventory = new InventoryDAO();
    private SupplierDAO supplier = new SupplierDAO();
    private CategoryDAO category = new CategoryDAO();

    private String imageUrl;

    @FXML
    void handleSortTable(ActionEvent event) {
        // Implementation for table sorting if needed
    }

    @FXML
    void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());

        if (file != null) {
            imageUrl = file.toURI().toString();
            try {
                Image image = new Image(imageUrl);
                productImageView.setImage(image);
            } catch (Exception e) {
                showErrorAlert("Lỗi", "Không thể tải hình ảnh: " + e.getMessage());
            }
        } else {
            System.out.println("Không có hình ảnh nào được chọn!");
        }
    }

    @FXML
    void handleAddProduct(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        try {
            Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
            if (selectedCategory == null) {
                showErrorAlert("Lỗi", "Vui lòng chọn danh mục sản phẩm!");
                return;
            }

            int productId = Integer.parseInt(productIdField.getText().trim());
            String productName = productNameField.getText().trim();
            String description = descriptionArea.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stockQuantity = Integer.parseInt(stockQuantityField.getText().trim());

            // Validate positive values
            if (price < 0) {
                showErrorAlert("Lỗi", "Giá sản phẩm không thể âm!");
                return;
            }
            if (stockQuantity < 0) {
                showErrorAlert("Lỗi", "Số lượng tồn kho không thể âm!");
                return;
            }

            // Create and add product
            Product newProduct = new Product(productId, productName, description, price,
                    imageUrl != null ? imageUrl : "", selectedCategory);

            boolean productAdded = product.addProduct(newProduct);
            if (productAdded) {
                inventory.updateStock(productId, stockQuantity);

                // Handle supplier logic
                String supplierName = unitField.getText().trim();
                if (!supplierName.isEmpty() && supplier.getSuppliersByName(supplierName).size() == 0) {
                    // TODO: Open supplier form to add new supplier
                    // For now, just show a message
                    showInfoAlert("Thông báo", "Nhà cung cấp mới sẽ cần được thêm vào hệ thống!");
                }

                refreshProductTable();
                clearForm();
                showInfoAlert("Thành công", "Đã thêm sản phẩm thành công!");
            } else {
                showErrorAlert("Lỗi", "Không thể thêm sản phẩm. ID có thể đã tồn tại!");
            }

        } catch (NumberFormatException e) {
            showErrorAlert("Lỗi", "Vui lòng nhập đúng định dạng số cho ID, giá và số lượng!");
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateProduct(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        try {
            Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
            if (selectedCategory == null) {
                showErrorAlert("Lỗi", "Vui lòng chọn danh mục sản phẩm!");
                return;
            }

            int productId = Integer.parseInt(productIdField.getText().trim());
            String productName = productNameField.getText().trim();
            String description = descriptionArea.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stockQuantity = Integer.parseInt(stockQuantityField.getText().trim());

            // Validate positive values
            if (price < 0) {
                showErrorAlert("Lỗi", "Giá sản phẩm không thể âm!");
                return;
            }
            if (stockQuantity < 0) {
                showErrorAlert("Lỗi", "Số lượng tồn kho không thể âm!");
                return;
            }

            Product updatedProduct = new Product(productId, productName, description, price,
                    imageUrl != null ? imageUrl : "", selectedCategory);

            boolean productUpdated = product.updateProduct(updatedProduct);
            if (productUpdated) {
                inventory.updateStock(productId, stockQuantity);
                refreshProductTable();
                showInfoAlert("Thành công", "Đã cập nhật sản phẩm thành công!");
            } else {
                showErrorAlert("Lỗi", "Không thể cập nhật sản phẩm!");
            }

        } catch (NumberFormatException e) {
            showErrorAlert("Lỗi", "Vui lòng nhập đúng định dạng số cho ID, giá và số lượng!");
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteProduct(ActionEvent event) {
        if (productIdField.getText().trim().isEmpty()) {
            showErrorAlert("Lỗi", "Vui lòng nhập ID sản phẩm cần xóa!");
            return;
        }

        try {
            int productId = Integer.parseInt(productIdField.getText().trim());

            // Show confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận xóa");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa sản phẩm này?");
            confirmAlert.setContentText("Hành động này không thể hoàn tác!");

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                boolean productDeleted = product.deleteProduct(productId);
                if (productDeleted) {
                    inventory.deleteStock(productId);
                    refreshProductTable();
                    clearForm();
                    showInfoAlert("Thành công", "Đã xóa sản phẩm thành công!");
                } else {
                    showErrorAlert("Lỗi", "Không thể xóa sản phẩm!");
                }
            }

        } catch (NumberFormatException e) {
            showErrorAlert("Lỗi", "ID sản phẩm phải là số!");
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    public void initialize() {
        // Set up product table
        setProductTable();

        // Load categories into ComboBox
        loadCategories();

        // Add table selection listener
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFormWithProduct(newValue);
                    }
                }
        );
    }

    private void setProductTable() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Custom cell value factory for stock column
        stockColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            InventoryItem inventoryItem = inventory.getInventoryByProductId(product.getProductId());
            return new javafx.beans.property.SimpleIntegerProperty(
                    inventoryItem != null ? inventoryItem.getQuantityOnHand() : 0
            ).asObject();
        });

        // Custom cell value factory for supplier column
        supplierColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            // This assumes you have a method to get supplier by product
            // You might need to modify this based on your actual data structure
            return new javafx.beans.property.SimpleStringProperty("N/A"); // Placeholder
        });

        refreshProductTable();
    }

    private void loadCategories() {
        try {
            ObservableList<Category> categories = FXCollections.observableArrayList(
                    category.getAllCategories()
            );
            categoryComboBox.setItems(categories);
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Không thể tải danh sách danh mục: " + e.getMessage());
        }
    }

    private void refreshProductTable() {
        try {
            productList = FXCollections.observableArrayList(product.getAllProductsWithCategory());
            productTable.setItems(productList);
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Không thể tải danh sách sản phẩm: " + e.getMessage());
        }
    }

    private void populateFormWithProduct(Product selectedProduct) {
        productIdField.setText(String.valueOf(selectedProduct.getProductId()));
        productNameField.setText(selectedProduct.getProductName());
        descriptionArea.setText(selectedProduct.getDescription());
        priceField.setText(String.valueOf(selectedProduct.getPrice()));

        // Set category
        categoryComboBox.getSelectionModel().select(selectedProduct.getCategory());

        // Load image if exists
        if (selectedProduct.getImageUrl() != null && !selectedProduct.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(selectedProduct.getImageUrl());
                productImageView.setImage(image);
                imageUrl = selectedProduct.getImageUrl();
            } catch (Exception e) {
                System.out.println("Không thể tải hình ảnh: " + e.getMessage());
            }
        }

        // Load stock quantity
        try {
            InventoryItem inventoryItem = inventory.getInventoryByProductId(selectedProduct.getProductId());
            if (inventoryItem != null) {
                stockQuantityField.setText(String.valueOf(inventoryItem.getQuantityOnHand()));
            }
        } catch (Exception e) {
            System.out.println("Không thể tải thông tin tồn kho: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (productIdField.getText().trim().isEmpty()) {
            showErrorAlert("Lỗi", "Vui lòng nhập ID sản phẩm!");
            return false;
        }
        if (productNameField.getText().trim().isEmpty()) {
            showErrorAlert("Lỗi", "Vui lòng nhập tên sản phẩm!");
            return false;
        }
        if (priceField.getText().trim().isEmpty()) {
            showErrorAlert("Lỗi", "Vui lòng nhập giá sản phẩm!");
            return false;
        }
        if (stockQuantityField.getText().trim().isEmpty()) {
            showErrorAlert("Lỗi", "Vui lòng nhập số lượng tồn kho!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        productIdField.clear();
        productNameField.clear();
        descriptionArea.clear();
        priceField.clear();
        stockQuantityField.clear();
        unitField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        productImageView.setImage(null);
        imageUrl = null;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}