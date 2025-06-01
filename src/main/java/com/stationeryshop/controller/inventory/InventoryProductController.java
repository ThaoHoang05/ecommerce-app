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
import javafx.util.StringConverter;

import com.stationeryshop.model.Product;
import com.stationeryshop.model.Category;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

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
    private TableColumn<Product, Integer> stockColumn;

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
    private TableColumn<Product, Double> priceColumn;

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

        // Set up category ComboBox display
        setupCategoryComboBox();

        // Add table selection listener
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFormWithProduct(newValue);
                    }
                }
        );
    }

    private void setupCategoryComboBox() {
        // Set up StringConverter for proper display of Category objects
        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                if (category == null) {
                    return null;
                } else {
                    return category.getCategoryName();
                }
            }

            @Override
            public Category fromString(String string) {
                // This is used when the user types in the ComboBox (if editable)
                // For now, return null as we're using selection only
                return null;
            }
        });

        // Set up cell factory for proper display in the dropdown
        categoryComboBox.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell<Category> call(ListView<Category> param) {
                return new ListCell<Category>() {
                    @Override
                    protected void updateItem(Category item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getCategoryName());
                        }
                    }
                };
            }
        });

        // Set up button cell for the ComboBox display
        categoryComboBox.setButtonCell(new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Chọn danh mục...");
                } else {
                    setText(item.getCategoryName());
                }
            }
        });
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
            try {
                InventoryItem inventoryItem = inventory.getInventoryByProductId(product.getProductId());
                return new javafx.beans.property.SimpleIntegerProperty(
                        inventoryItem != null ? inventoryItem.getQuantityOnHand() : 0
                ).asObject();
            } catch (Exception e) {
                System.out.println("Error loading inventory for product " + product.getProductId() + ": " + e.getMessage());
                return new javafx.beans.property.SimpleIntegerProperty(0).asObject();
            }
        });

        // Custom cell value factory for supplier column
        supplierColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            // This assumes you have a method to get supplier by product
            // You might need to modify this based on your actual data structure
            try {
                // TODO: Implement actual supplier lookup logic
                return new javafx.beans.property.SimpleStringProperty("N/A");
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty("Error");
            }
        });

        refreshProductTable();
    }

    private void loadCategories() {
        try {
            List<Category> categoryList = category.getAllCategories();
            if (categoryList != null && !categoryList.isEmpty()) {
                ObservableList<Category> categories = FXCollections.observableArrayList(categoryList);
                categoryComboBox.setItems(categories);
            } else {
                showErrorAlert("Cảnh báo", "Không có danh mục nào trong hệ thống. Vui lòng thêm danh mục trước!");
            }
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Không thể tải danh sách danh mục: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshProductTable() {
        try {
            List<Product> products = product.getAllProductsWithCategory();
            if (products != null) {
                productList = FXCollections.observableArrayList(products);
                productTable.setItems(productList);
            }
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Không thể tải danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateFormWithProduct(Product selectedProduct) {
        try {
            productIdField.setText(String.valueOf(selectedProduct.getProductId()));
            productNameField.setText(selectedProduct.getProductName());
            descriptionArea.setText(selectedProduct.getDescription());
            priceField.setText(String.valueOf(selectedProduct.getPrice()));

            // Set category in ComboBox
            Category productCategory = selectedProduct.getCategory();
            if (productCategory != null) {
                // Find the matching category in the ComboBox items
                ObservableList<Category> categories = categoryComboBox.getItems();
                for (Category cat : categories) {
                    if (cat.getCategoryId() == productCategory.getCategoryId()) {
                        categoryComboBox.getSelectionModel().select(cat);
                        break;
                    }
                }
            } else {
                categoryComboBox.getSelectionModel().clearSelection();
            }

            // Load image if exists
            if (selectedProduct.getImageUrl() != null && !selectedProduct.getImageUrl().isEmpty()) {
                try {
                    Image image = new Image(selectedProduct.getImageUrl());
                    productImageView.setImage(image);
                    imageUrl = selectedProduct.getImageUrl();
                } catch (Exception e) {
                    System.out.println("Không thể tải hình ảnh: " + e.getMessage());
                    productImageView.setImage(null);
                    imageUrl = null;
                }
            } else {
                productImageView.setImage(null);
                imageUrl = null;
            }

            // Load stock quantity
            try {
                InventoryItem inventoryItem = inventory.getInventoryByProductId(selectedProduct.getProductId());
                if (inventoryItem != null) {
                    stockQuantityField.setText(String.valueOf(inventoryItem.getQuantityOnHand()));
                } else {
                    stockQuantityField.setText("0");
                }
            } catch (Exception e) {
                System.out.println("Không thể tải thông tin tồn kho: " + e.getMessage());
                stockQuantityField.setText("0");
            }
        } catch (Exception e) {
            showErrorAlert("Lỗi", "Có lỗi xảy ra khi tải thông tin sản phẩm: " + e.getMessage());
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
        if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("Lỗi", "Vui lòng chọn danh mục sản phẩm!");
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