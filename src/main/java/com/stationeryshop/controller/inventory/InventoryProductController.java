package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryProductDAO;
import com.stationeryshop.dao.SupplierDAO;
import com.stationeryshop.model.Category;
import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.model.Supplier;
import com.stationeryshop.utils.ThreadUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class InventoryProductController implements Initializable {

    @FXML
    private TextField productNameField;

    @FXML
    private Button addProductBtn;

    @FXML
    private TextField unitField;

    @FXML
    private TextField searchField;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button searchBtn;

    @FXML
    private TableView<InventoryProduct> productTable;

    @FXML
    private TableColumn<InventoryProduct, String> categoryColumn;

    @FXML
    private Button deleteProductBtn;

    @FXML
    private TableColumn<InventoryProduct, Integer> stockColumn;

    @FXML
    private Button updateProductBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private ImageView productImageView;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private ComboBox<Supplier> supplierComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableColumn<InventoryProduct, String> nameColumn;

    @FXML
    private TextField productIdField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockQuantityField;

    @FXML
    private TextField supplyPriceField;

    @FXML
    private TableColumn<InventoryProduct, String> supplierColumn;

    @FXML
    private TableColumn<InventoryProduct, Double> priceColumn;

    @FXML
    private TableColumn<InventoryProduct, Integer> idColumn;

    @FXML
    private TableColumn<InventoryProduct, String> descriptionColumn;

    // DAO instances
    private InventoryProductDAO inventoryProductDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;

    // Data
    private ObservableList<InventoryProduct> productList;
    private String selectedImagePath = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize DAOs
        inventoryProductDAO = new InventoryProductDAO();
        categoryDAO = new CategoryDAO();
        supplierDAO = new SupplierDAO();

        // Setup table columns
        setupTableColumns();

        // Load data
        loadCategories();       // luong 1
        loadSuppliers();        //luong 2
        loadProducts();         //luong 3

        // Setup table selection listener
        setupTableSelectionListener();

        // Disable edit/delete buttons initially
        updateProductBtn.setDisable(true);
        deleteProductBtn.setDisable(true);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        // Custom cell value factory for supplier names
        supplierColumn.setCellValueFactory(cellData -> {
            String suppliers = cellData.getValue().getSupplierName();
            String supplierNames = String.join(", ", suppliers);
            return new javafx.beans.property.SimpleStringProperty(supplierNames);
        });
    }

    private void setupTableSelectionListener() {
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
                updateProductBtn.setDisable(false);
                deleteProductBtn.setDisable(false);
            } else {
                clearFields();
                updateProductBtn.setDisable(true);
                deleteProductBtn.setDisable(true);
            }
        });
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));

            // Set display converter for ComboBox
            categoryComboBox.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category != null ? category.getCategoryName() : "";
                }

                @Override
                public Category fromString(String string) {
                    return categoryComboBox.getItems().stream()
                            .filter(item -> item.getCategoryName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải danh sách danh mục: " + e.getMessage());
        }
    }

    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            supplierComboBox.setItems(FXCollections.observableArrayList(suppliers));

            // Set display converter for ComboBox
            supplierComboBox.setConverter(new StringConverter<Supplier>() {
                @Override
                public String toString(Supplier supplier) {
                    return supplier != null ? supplier.getSupplierName() : "";
                }

                @Override
                public Supplier fromString(String string) {
                    return supplierComboBox.getItems().stream()
                            .filter(item -> item.getSupplierName().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải danh sách nhà cung cấp: " + e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            productList = inventoryProductDAO.getAllInventoryProduct();
            productTable.setItems(productList);
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể tải danh sách sản phẩm: " + e.getMessage());
        }
    }

    private void populateFields(InventoryProduct product) {
        productIdField.setText(String.valueOf(product.getProductId()));
        productNameField.setText(product.getProductName());
        descriptionArea.setText(product.getProductDescription());
        priceField.setText(String.valueOf(product.getProductPrice()));
        stockQuantityField.setText(String.valueOf(product.getQuantity()));

        // Set category
        Category selectedCategory = categoryComboBox.getItems().stream()
                .filter(cat -> cat.getCategoryName().equals(product.getCategoryName()))
                .findFirst()
                .orElse(null);
        categoryComboBox.setValue(selectedCategory);

        // Set supplier (first supplier if multiple)
        String supplierNames = product.getSupplierName();
        if (!supplierNames.isEmpty()) {
            Supplier selectedSupplier = supplierComboBox.getItems().stream()
                    .filter(sup -> sup.getSupplierName().equals(supplierNames))
                    .findFirst()
                    .orElse(null);
            supplierComboBox.setValue(selectedSupplier);
        }

        // Load image if exists
        // loadProductImage(product.getImageUrl());
    }

    private void clearFields() {
        productIdField.clear();
        productNameField.clear();
        descriptionArea.clear();
        priceField.clear();
        stockQuantityField.clear();
        supplyPriceField.clear();
        categoryComboBox.setValue(null);
        supplierComboBox.setValue(null);
        productImageView.setImage(null);
        selectedImagePath = "";
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().trim().toLowerCase();

        if (searchTerm.isEmpty()) {
            productTable.setItems(productList);
            return;
        }

        ObservableList<InventoryProduct> filteredList = productList.filtered(product ->
                product.getProductName().toLowerCase().contains(searchTerm) ||
                        product.getProductDescription().toLowerCase().contains(searchTerm) ||
                        product.getCategoryName().toLowerCase().contains(searchTerm)
        );

        productTable.setItems(filteredList);
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        loadProducts();
        clearFields();
        searchField.clear();
        showInfo("Thành công", "Đã làm mới danh sách sản phẩm");
    }

    @FXML
    void handleSortTable(ActionEvent event) {
        // Toggle sort by name
        if (nameColumn.getSortType() == TableColumn.SortType.ASCENDING) {
            nameColumn.setSortType(TableColumn.SortType.DESCENDING);
        } else {
            nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        }
        productTable.getSortOrder().clear();
        productTable.getSortOrder().add(nameColumn);
        productTable.sort();
    }

    @FXML
    void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn hình ảnh sản phẩm");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();

            try {
                Image image = new Image(selectedFile.toURI().toString());
                productImageView.setImage(image);
            } catch (Exception e) {
                showAlert("Lỗi", "Không thể tải hình ảnh: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleAddProduct(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            String name = productNameField.getText().trim();
            String description = descriptionArea.getText().trim();
            float price = Float.parseFloat(priceField.getText().trim());
            int quantity = Integer.parseInt(stockQuantityField.getText().trim());
            float supplyPrice = Float.parseFloat(supplyPriceField.getText().trim());
            String categoryName = categoryComboBox.getValue().getCategoryName();
            String supplierName = supplierComboBox.getValue().getSupplierName();
            String imagePath = selectedImagePath.isEmpty() ? "default.png" : selectedImagePath;

            inventoryProductDAO.addProduct(name, description, price, quantity, categoryName, imagePath, supplierName, supplyPrice);

            showInfo("Thành công", "Đã thêm sản phẩm thành công");
            clearFields();
            loadProducts();

        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Vui lòng nhập đúng định dạng số cho giá và số lượng");
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể thêm sản phẩm: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateProduct(ActionEvent event) {
        InventoryProduct selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Lỗi", "Vui lòng chọn sản phẩm để cập nhật");
            return;
        }

        if (!validateInput()) {
            return;
        }

        try {
            int productId = selectedProduct.getProductId();
            String name = productNameField.getText().trim();
            String description = descriptionArea.getText().trim();
            float price = Float.parseFloat(priceField.getText().trim());
            int quantity = Integer.parseInt(stockQuantityField.getText().trim());
            float supplyPrice = Float.parseFloat(supplyPriceField.getText().trim());
            String categoryName = categoryComboBox.getValue().getCategoryName();
            String supplierName = supplierComboBox.getValue().getSupplierName();
            String imagePath = selectedImagePath.isEmpty() ? "default.png" : selectedImagePath;

            inventoryProductDAO.updateProduct(productId, name, description, price, quantity, categoryName, imagePath, supplierName, supplyPrice);

            showInfo("Thành công", "Đã cập nhật sản phẩm thành công");
            clearFields();
            loadProducts();

        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Vui lòng nhập đúng định dạng số cho giá và số lượng");
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể cập nhật sản phẩm: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteProduct(ActionEvent event) {
        InventoryProduct selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Lỗi", "Vui lòng chọn sản phẩm để xóa");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa sản phẩm này?");
        confirmAlert.setContentText("Sản phẩm: " + selectedProduct.getProductName());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                inventoryProductDAO.deleteProduct(selectedProduct.getProductId());
                showInfo("Thành công", "Đã xóa sản phẩm thành công");
                clearFields();
                loadProducts();
            } catch (Exception e) {
                showAlert("Lỗi", "Không thể xóa sản phẩm: " + e.getMessage());
            }
        }
    }

    private boolean validateInput() {
        if (productNameField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập tên sản phẩm");
            return false;
        }

        if (priceField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập giá sản phẩm");
            return false;
        }

        if (stockQuantityField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập số lượng tồn kho");
            return false;
        }

        if (supplyPriceField.getText().trim().isEmpty()) {
            showAlert("Lỗi", "Vui lòng nhập giá nhập");
            return false;
        }

        if (categoryComboBox.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn danh mục");
            return false;
        }

        if (supplierComboBox.getValue() == null) {
            showAlert("Lỗi", "Vui lòng chọn nhà cung cấp");
            return false;
        }

        try {
            Float.parseFloat(priceField.getText().trim());
            Integer.parseInt(stockQuantityField.getText().trim());
            Float.parseFloat(supplyPriceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Vui lòng nhập đúng định dạng số");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}