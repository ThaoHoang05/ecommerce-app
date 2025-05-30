package com.stationeryshop.controller;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.Category;
import com.stationeryshop.model.InventoryItem;
import com.stationeryshop.model.Product;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    // Product Tab Controls
    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button refreshBtn;
    
    @FXML private ImageView productImageView;
    @FXML private Button selectImageButton;
    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField priceField;
    @FXML private TextField stockQuantityField;
    @FXML private TextField unitField;
    @FXML private TextArea descriptionArea;
    
    @FXML private Button addProductBtn;
    @FXML private Button updateProductBtn;
    @FXML private Button deleteProductBtn;
    
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;
    @FXML private TableColumn<Product, String> supplierColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;

    // Category Tab Controls
    @FXML private TextField categoryIdField;
    @FXML private TextField categoryNameField;
    @FXML private TextField categoryDescField;
    @FXML private Button addCategoryBtn;
    @FXML private Button updateCategoryBtn;
    @FXML private Button deleteCategoryBtn;
    
    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, Integer> catIdColumn;
    @FXML private TableColumn<Category, String> catNameColumn;
    @FXML private TableColumn<Category, String> catDescColumn;

    // Inventory Tab Controls
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, Integer> invIdColumn;
    @FXML private TableColumn<InventoryItem, String> invProductColumn;
    @FXML private TableColumn<InventoryItem, Integer> invQuantityColumn;
    @FXML private TableColumn<InventoryItem, String> invLastStockedColumn;

    // DAO instances
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private InventoryDAO inventoryDAO;

    // Observable Lists
    private ObservableList<Product> productList;
    private ObservableList<Category> categoryList;
    private ObservableList<InventoryItem> inventoryList;

    // Selected image path
    private String selectedImagePath = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize DAOs
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        inventoryDAO = new InventoryDAO();

        // Initialize Observable Lists
        productList = FXCollections.observableArrayList();
        categoryList = FXCollections.observableArrayList();
        inventoryList = FXCollections.observableArrayList();

        // Setup tables
        setupProductTable();
        setupCategoryTable();
        setupInventoryTable();
        
        // Setup ComboBox
        setupCategoryComboBox();

        // Load initial data
        loadAllData();

        // Setup table selection listeners
        setupTableSelectionListeners();
    }

    private void setupProductTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryColumn.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategory();
            return new javafx.beans.property.SimpleStringProperty(
                category != null ? category.getCategoryName() : "N/A"
            );
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(cellData -> {
            int productId = cellData.getValue().getProductId();
            int stock = inventoryDAO.getStockLevel(productId);
            return new javafx.beans.property.SimpleIntegerProperty(stock).asObject();
        });
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl")); // Using imageUrl as supplier field
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        productTable.setItems(productList);
    }

    private void setupCategoryTable() {
        catIdColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        catNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        catDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        categoryTable.setItems(categoryList);
    }

    private void setupInventoryTable() {
        invIdColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        invProductColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue().getProduct();
            return new javafx.beans.property.SimpleStringProperty(
                product != null ? product.getProductName() : "N/A"
            );
        });
        invQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));
        invLastStockedColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = cellData.getValue().getLastStockedDate().format(formatter);
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });

        inventoryTable.setItems(inventoryList);
    }

    private void setupCategoryComboBox() {
        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getCategoryName() : "";
            }

            @Override
            public Category fromString(String string) {
                return categoryComboBox.getItems().stream()
                    .filter(item -> item.getCategoryName().equals(string))
                    .findFirst().orElse(null);
            }
        });
    }

    private void setupTableSelectionListeners() {
        // Product table selection
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateProductFields(newSelection);
            }
        });

        // Category table selection
        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateCategoryFields(newSelection);
            }
        });
    }

    private void populateProductFields(Product product) {
        productIdField.setText(String.valueOf(product.getProductId()));
        productNameField.setText(product.getProductName());
        descriptionArea.setText(product.getDescription());
        priceField.setText(String.valueOf(product.getPrice()));
        unitField.setText(product.getImageUrl()); // Using imageUrl as supplier field
        
        if (product.getCategory() != null) {
            categoryComboBox.setValue(product.getCategory());
        }
        
        int stockLevel = inventoryDAO.getStockLevel(product.getProductId());
        stockQuantityField.setText(String.valueOf(stockLevel));

        // Load product image if exists
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                File imageFile = new File(product.getImageUrl());
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    productImageView.setImage(image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateCategoryFields(Category category) {
        categoryIdField.setText(String.valueOf(category.getCategoryId()));
        categoryNameField.setText(category.getCategoryName());
        categoryDescField.setText(category.getDescription());
    }

    private void loadAllData() {
        loadProducts();
        loadCategories();
        loadInventory();
    }

    private void loadProducts() {
        try {
            List<Product> products = productDAO.getAllProductsWithCategory();
            productList.clear();
            productList.addAll(products);
        } catch (Exception e) {
            showAlert("Error", "Failed to load products: " + e.getMessage());
        }
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategories();
            categoryList.clear();
            categoryList.addAll(categories);
            
            // Update ComboBox
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        } catch (Exception e) {
            showAlert("Error", "Failed to load categories: " + e.getMessage());
        }
    }

    private void loadInventory() {
        try {
            List<InventoryItem> inventory = inventoryDAO.getLowStockItems(1000); // Get all items
            inventoryList.clear();
            inventoryList.addAll(inventory);
        } catch (Exception e) {
            showAlert("Error", "Failed to load inventory: " + e.getMessage());
        }
    }

    // Product Management Methods
    @FXML
    private void handleAddProduct(ActionEvent event) {
        try {
            if (!validateProductFields()) return;

            Product product = new Product();
            product.setProductName(productNameField.getText().trim());
            product.setDescription(descriptionArea.getText().trim());
            product.setPrice(Double.parseDouble(priceField.getText().trim()));
            product.setCategory(categoryComboBox.getValue());
            product.setImageUrl(unitField.getText().trim()); // Using as supplier field
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            productDAO.addProduct(product);
            
            // Update stock if quantity is provided
            if (!stockQuantityField.getText().trim().isEmpty()) {
                int quantity = Integer.parseInt(stockQuantityField.getText().trim());
                // Note: This assumes the product ID is auto-generated and we need to get it
                // You might need to modify addProduct to return the generated ID
                if (quantity > 0) {
                    // inventoryDAO.updateStock(newProductId, quantity);
                }
            }

            clearProductFields();
            loadProducts();
            loadInventory();
            showAlert("Success", "Product added successfully!");
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for price and stock quantity.");
        } catch (Exception e) {
            showAlert("Error", "Failed to add product: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateProduct(ActionEvent event) {
        try {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                showAlert("Warning", "Please select a product to update.");
                return;
            }

            if (!validateProductFields()) return;

            selectedProduct.setProductName(productNameField.getText().trim());
            selectedProduct.setDescription(descriptionArea.getText().trim());
            selectedProduct.setPrice(Double.parseDouble(priceField.getText().trim()));
            selectedProduct.setCategory(categoryComboBox.getValue());
            selectedProduct.setImageUrl(unitField.getText().trim());
            selectedProduct.setUpdatedAt(LocalDateTime.now());

            productDAO.updateProduct(selectedProduct);

            // Update stock if changed
            if (!stockQuantityField.getText().trim().isEmpty()) {
                int currentStock = inventoryDAO.getStockLevel(selectedProduct.getProductId());
                int newStock = Integer.parseInt(stockQuantityField.getText().trim());
                int stockChange = newStock - currentStock;
                if (stockChange != 0) {
                    inventoryDAO.updateStock(selectedProduct.getProductId(), stockChange);
                }
            }

            loadProducts();
            loadInventory();
            showAlert("Success", "Product updated successfully!");
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for price and stock quantity.");
        } catch (Exception e) {
            showAlert("Error", "Failed to update product: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        try {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                showAlert("Warning", "Please select a product to delete.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Product");
            confirmAlert.setContentText("Are you sure you want to delete this product?");
            
            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                productDAO.deleteProduct(selectedProduct.getProductId());
                clearProductFields();
                loadProducts();
                loadInventory();
                showAlert("Success", "Product deleted successfully!");
            }
            
        } catch (Exception e) {
            showAlert("Error", "Failed to delete product: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        try {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                loadProducts();
                return;
            }

            List<Product> searchResults = productDAO.getProductsByName(searchTerm);
            productList.clear();
            productList.addAll(searchResults);
            
        } catch (Exception e) {
            showAlert("Error", "Search failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadAllData();
        clearProductFields();
        clearCategoryFields();
        searchField.clear();
    }

    @FXML
    private void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            try {
                Image image = new Image(selectedFile.toURI().toString());
                productImageView.setImage(image);
            } catch (Exception e) {
                showAlert("Error", "Failed to load image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSortTable(ActionEvent event) {
        // This method is called when table sorting occurs
        // Additional sorting logic can be added here if needed
    }

    // Category Management Methods
    @FXML
    private void handleAddCategory(ActionEvent event) {
        try {
            if (!validateCategoryFields()) return;

            Category category = new Category();
            category.setCategoryName(categoryNameField.getText().trim());
            category.setDescription(categoryDescField.getText().trim());

            categoryDAO.addCategory(category);
            clearCategoryFields();
            loadCategories();
            showAlert("Success", "Category added successfully!");
            
        } catch (Exception e) {
            showAlert("Error", "Failed to add category: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateCategory(ActionEvent event) {
        try {
            Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            if (selectedCategory == null) {
                showAlert("Warning", "Please select a category to update.");
                return;
            }

            if (!validateCategoryFields()) return;

            selectedCategory.setCategoryName(categoryNameField.getText().trim());
            selectedCategory.setDescription(categoryDescField.getText().trim());

            categoryDAO.updateCategory(selectedCategory);
            loadCategories();
            showAlert("Success", "Category updated successfully!");
            
        } catch (Exception e) {
            showAlert("Error", "Failed to update category: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCategory(ActionEvent event) {
        try {
            Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            if (selectedCategory == null) {
                showAlert("Warning", "Please select a category to delete.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Category");
            confirmAlert.setContentText("Are you sure you want to delete this category?");
            
            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                categoryDAO.deleteCategory(selectedCategory.getCategoryId());
                clearCategoryFields();
                loadCategories();
                showAlert("Success", "Category deleted successfully!");
            }
            
        } catch (Exception e) {
            showAlert("Error", "Failed to delete category: " + e.getMessage());
        }
    }

    // Validation Methods
    private boolean validateProductFields() {
        if (productNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Product name is required.");
            return false;
        }
        
        try {
            Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid price.");
            return false;
        }
        
        if (!stockQuantityField.getText().trim().isEmpty()) {
            try {
                Integer.parseInt(stockQuantityField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Please enter a valid stock quantity.");
                return false;
            }
        }
        
        return true;
    }

    private boolean validateCategoryFields() {
        if (categoryNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Category name is required.");
            return false;
        }
        return true;
    }

    // Utility Methods
    private void clearProductFields() {
        productIdField.clear();
        productNameField.clear();
        descriptionArea.clear();
        priceField.clear();
        stockQuantityField.clear();
        unitField.clear();
        categoryComboBox.setValue(null);
        productImageView.setImage(null);
        selectedImagePath = "";
    }

    private void clearCategoryFields() {
        categoryIdField.clear();
        categoryNameField.clear();
        categoryDescField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
