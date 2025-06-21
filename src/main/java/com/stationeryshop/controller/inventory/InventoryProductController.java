package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryProductDAO;
import com.stationeryshop.dao.SupplierDAO;
import com.stationeryshop.model.Category;
import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.model.Supplier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import java.util.concurrent.CompletableFuture;

public class InventoryProductController implements Initializable {

    @FXML private TextField productNameField;
    @FXML private Button addProductBtn;
    @FXML private TextField supplyPriceField;
    @FXML private TextField searchField;
    @FXML private Button selectImageButton;
    @FXML private Button searchBtn;
    @FXML private TableView<InventoryProduct> productTable;
    @FXML private TableColumn<InventoryProduct, String> categoryColumn;
    @FXML private Button deleteProductBtn;
    @FXML private TableColumn<InventoryProduct, Integer> stockColumn;
    @FXML private Button updateProductBtn;
    @FXML private Button refreshBtn;
    @FXML private ImageView productImageView;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<Supplier> supplierComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private TableColumn<InventoryProduct, String> nameColumn;
    @FXML private TextField productIdField;
    @FXML private TextField priceField;
    @FXML private TextField stockQuantityField;
    @FXML private TableColumn<InventoryProduct, String> supplierColumn;
    @FXML private TableColumn<InventoryProduct, Double> priceColumn;
    @FXML private TableColumn<InventoryProduct, Integer> idColumn;
    @FXML private TableColumn<InventoryProduct, String> descriptionColumn;

    // DAO instances
    private InventoryProductDAO inventoryProductDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;

    // Data caching
    private ObservableList<InventoryProduct> productList;
    private ObservableList<Category> categoryList;
    private ObservableList<Supplier> supplierList;
    private String selectedImagePath = "";

    // Loading indicator
    @FXML private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize DAOs
        inventoryProductDAO = new InventoryProductDAO();
        categoryDAO = new CategoryDAO();
        supplierDAO = new SupplierDAO();

        // Setup table columns immediately (UI thread)
        setupTableColumns();
        setupTableSelectionListener();

        // Disable buttons initially
        updateProductBtn.setDisable(true);
        deleteProductBtn.setDisable(true);

        // Load data asynchronously to avoid blocking UI
        loadDataAsync();
    }

    /**
     * Load data asynchronously to prevent UI blocking
     */
    private void loadDataAsync() {
        // Show loading indicator if available
        if (loadingIndicator != null) {
            loadingIndicator.setVisible(true);
        }

        // Load all data in parallel using CompletableFuture
        CompletableFuture<List<Category>> categoriesTask = CompletableFuture.supplyAsync(() -> {
            try {
                return categoryDAO.getAllCategories();
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Lỗi", "Không thể tải danh mục: " + e.getMessage()));
                return FXCollections.emptyObservableList();
            }
        });

        CompletableFuture<List<Supplier>> suppliersTask = CompletableFuture.supplyAsync(() -> {
            try {
                return supplierDAO.getAllSuppliers();
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Lỗi", "Không thể tải nhà cung cấp: " + e.getMessage()));
                return FXCollections.emptyObservableList();
            }
        });

        CompletableFuture<ObservableList<InventoryProduct>> productsTask = CompletableFuture.supplyAsync(() -> {
            try {
                return inventoryProductDAO.getAllInventoryProduct();
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Lỗi", "Không thể tải sản phẩm: " + e.getMessage()));
                return FXCollections.emptyObservableList();
            }
        });

        // Wait for all tasks to complete and update UI
        CompletableFuture.allOf(categoriesTask, suppliersTask, productsTask)
                .thenRun(() -> {
                    Platform.runLater(() -> {
                        try {
                            // Update UI with loaded data
                            categoryList = FXCollections.observableArrayList(categoriesTask.get());
                            supplierList = FXCollections.observableArrayList(suppliersTask.get());
                            productList = productsTask.get();

                            setupCategoryComboBox();
                            setupSupplierComboBox();
                            productTable.setItems(productList);

                            // Hide loading indicator
                            if (loadingIndicator != null) {
                                loadingIndicator.setVisible(false);
                            }
                        } catch (Exception e) {
                            showAlert("Lỗi", "Lỗi khi cập nhật giao diện: " + e.getMessage());
                        }
                    });
                });
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
            return new javafx.beans.property.SimpleStringProperty(suppliers);
        });

        // Optimize table performance
        productTable.setRowFactory(tv -> {
            TableRow<InventoryProduct> row = new TableRow<>();
            // Only create context menu when needed
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    // Create context menu on demand
                }
            });
            return row;
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

    private void setupCategoryComboBox() {
        categoryComboBox.setItems(categoryList);
        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getCategoryName() : "";
            }

            @Override
            public Category fromString(String string) {
                return categoryList.stream()
                        .filter(item -> item.getCategoryName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void setupSupplierComboBox() {
        supplierComboBox.setItems(supplierList);
        supplierComboBox.setConverter(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier supplier) {
                return supplier != null ? supplier.getSupplierName() : "";
            }

            @Override
            public Supplier fromString(String string) {
                return supplierList.stream()
                        .filter(item -> item.getSupplierName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    /**
     * Reload products asynchronously
     */
    private void loadProducts() {
        Task<ObservableList<InventoryProduct>> task = new Task<ObservableList<InventoryProduct>>() {
            @Override
            protected ObservableList<InventoryProduct> call() throws Exception {
                return inventoryProductDAO.getAllInventoryProduct();
            }

            @Override
            protected void succeeded() {
                productList = getValue();
                productTable.setItems(productList);
            }

            @Override
            protected void failed() {
                showAlert("Lỗi", "Không thể tải danh sách sản phẩm: " + getException().getMessage());
            }
        };

        new Thread(task).start();
    }

    private void populateFields(InventoryProduct product) {
        productIdField.setText(String.valueOf(product.getProductId()));
        productNameField.setText(product.getProductName());
        descriptionArea.setText(product.getProductDescription());
        supplyPriceField.setText(String.valueOf(product.getSupplyPrice()));
        priceField.setText(String.valueOf(product.getProductPrice()));
        stockQuantityField.setText(String.valueOf(product.getQuantity()));

        // Set category
        Category selectedCategory = categoryList.stream()
                .filter(cat -> cat.getCategoryName().equals(product.getCategoryName()))
                .findFirst()
                .orElse(null);
        categoryComboBox.setValue(selectedCategory);

        // Set supplier
        String supplierNames = product.getSupplierName();
        if (!supplierNames.isEmpty()) {
            Supplier selectedSupplier = supplierList.stream()
                    .filter(sup -> sup.getSupplierName().equals(supplierNames))
                    .findFirst()
                    .orElse(null);
            supplierComboBox.setValue(selectedSupplier);
        }

        // Load image asynchronously to avoid blocking UI
        loadProductImageAsync(product.getImageUrl());
    }
    public void refreshDataBar(boolean choice){
        this.loadDataAsync();
    }
    /**
     * Load product image asynchronously
     */
    private void loadProductImageAsync(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Task<Image> imageTask = new Task<Image>() {
                @Override
                protected Image call() throws Exception {
                    String url = "file:/" + imageUrl.replace("\\", "/");
                    return new Image(url, true); // Load in background
                }

                @Override
                protected void succeeded() {
                    productImageView.setImage(getValue());
                }

                @Override
                protected void failed() {
                    // Set default image or leave empty
                    productImageView.setImage(null);
                }
            };

            new Thread(imageTask).start();
        } else {
            productImageView.setImage(null);
        }
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

        // Use filtered list for better performance
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
        showInfo("Thành công", "Đang làm mới danh sách sản phẩm...");
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

            // Load image asynchronously
            Task<Image> imageTask = new Task<Image>() {
                @Override
                protected Image call() throws Exception {
                    return new Image(selectedFile.toURI().toString(), true);
                }

                @Override
                protected void succeeded() {
                    productImageView.setImage(getValue());
                }

                @Override
                protected void failed() {
                    showAlert("Lỗi", "Không thể tải hình ảnh: " + getException().getMessage());
                }
            };

            new Thread(imageTask).start();
        }
    }

    // Database operations remain the same but should also be made async
    @FXML
    void handleAddProduct(ActionEvent event) {
        if (!validateInput()) return;

        Task<Void> addTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String name = productNameField.getText().trim();
                String description = descriptionArea.getText().trim();
                float price = Float.parseFloat(priceField.getText().trim());
                int quantity = Integer.parseInt(stockQuantityField.getText().trim());
                float supplyPrice = Float.parseFloat(supplyPriceField.getText().trim());
                String categoryName = categoryComboBox.getValue().getCategoryName();
                String supplierName = supplierComboBox.getValue().getSupplierName();
                String imagePath = selectedImagePath.isEmpty() ? "default.png" : selectedImagePath;

                inventoryProductDAO.addProduct(name, description, price, quantity, categoryName, imagePath, supplierName, supplyPrice);
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    showInfo("Thành công", "Đã thêm sản phẩm thành công");
                    clearFields();
                    loadProducts();
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    Throwable exception = getException();
                    if (exception instanceof NumberFormatException) {
                        showAlert("Lỗi", "Vui lòng nhập đúng định dạng số cho giá và số lượng");
                    } else {
                        showAlert("Lỗi", "Không thể thêm sản phẩm: " + exception.getMessage());
                    }
                });
            }
        };

        new Thread(addTask).start();
    }

    @FXML
    void handleUpdateProduct(ActionEvent event) {
        InventoryProduct selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Lỗi", "Vui lòng chọn sản phẩm để cập nhật");
            return;
        }

        if (!validateInput()) return;

        Task<Void> updateTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
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
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    showInfo("Thành công", "Đã cập nhật sản phẩm thành công");
                    clearFields();
                    loadProducts();
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    Throwable exception = getException();
                    if (exception instanceof NumberFormatException) {
                        showAlert("Lỗi", "Vui lòng nhập đúng định dạng số cho giá và số lượng");
                    } else {
                        showAlert("Lỗi", "Không thể cập nhật sản phẩm: " + exception.getMessage());
                    }
                });
            }
        };

        new Thread(updateTask).start();
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
            Task<Void> deleteTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    inventoryProductDAO.deleteProduct(selectedProduct.getProductId());
                    return null;
                }

                @Override
                protected void succeeded() {
                    Platform.runLater(() -> {
                        showInfo("Thành công", "Đã xóa sản phẩm thành công");
                        clearFields();
                        loadProducts();
                    });
                }

                @Override
                protected void failed() {
                    Platform.runLater(() ->
                            showAlert("Lỗi", "Không thể xóa sản phẩm: " + getException().getMessage())
                    );
                }
            };

            new Thread(deleteTask).start();
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