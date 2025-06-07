package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.CategoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import com.stationeryshop.model.Category;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryCategoryController {

    @FXML
    private TextField categoryDescField;

    @FXML
    private TextField categoryIdField;

    @FXML
    private Button addCategoryBtn;

    @FXML
    private TableColumn<Category,String> catNameColumn;

    @FXML
    private TextField categoryNameField;

    @FXML
    private TableColumn<Category,Integer> catIdColumn;

    @FXML
    private Button updateCategoryBtn;

    @FXML
    private Button deleteCategoryBtn;

    @FXML
    private TableColumn<Category, String> catDescColumn;

    @FXML
    private TableView<Category> categoryTable;

    private ObservableList<Category> categoryList;

    private CategoryDAO categoryDAO = new CategoryDAO();

    @FXML
    void handleAddCategory(ActionEvent event) {
        try {
            // Validate input
            String categoryName = categoryNameField.getText().trim();
            String categoryDesc = categoryDescField.getText().trim();

            if (categoryName.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập tên danh mục!", Alert.AlertType.ERROR);
                return;
            }

            // For add operation, we don't need ID from user input (auto-generated)
            Category newCategory = new Category(0, categoryName, categoryDesc);
            categoryDAO.addCategory(newCategory);

            // Refresh table and clear fields
            refreshTable();
            clearFields();
            showAlert("Thành công", "Thêm danh mục thành công!", Alert.AlertType.INFORMATION);

        } catch(Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Có lỗi xảy ra khi thêm danh mục: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleUpdateCategory(ActionEvent event) {
        try {
            // Validate input
            String categoryIdText = categoryIdField.getText().trim();
            String categoryName = categoryNameField.getText().trim();
            String categoryDesc = categoryDescField.getText().trim();

            if (categoryIdText.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập ID danh mục để cập nhật!", Alert.AlertType.ERROR);
                return;
            }

            if (categoryName.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập tên danh mục!", Alert.AlertType.ERROR);
                return;
            }

            int categoryId = Integer.parseInt(categoryIdText);
            Category updatedCategory = new Category(categoryId, categoryName, categoryDesc);
            categoryDAO.updateCategory(updatedCategory);

            // Refresh table and clear fields
            refreshTable();
            clearFields();
            showAlert("Thành công", "Cập nhật danh mục thành công!", Alert.AlertType.INFORMATION);

        } catch(NumberFormatException e) {
            showAlert("Lỗi", "ID danh mục phải là số!", Alert.AlertType.ERROR);
        } catch(Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Có lỗi xảy ra khi cập nhật danh mục: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleDeleteCategory(ActionEvent event) {
        try {
            String categoryIdText = categoryIdField.getText().trim();

            if (categoryIdText.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập ID danh mục để xóa!", Alert.AlertType.ERROR);
                return;
            }

            int categoryId = Integer.parseInt(categoryIdText);

            // Confirm deletion
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận xóa");
            confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa danh mục này?");
            confirmAlert.setContentText("Hành động này không thể hoàn tác!");

            if (confirmAlert.showAndWait().get().getButtonData().isCancelButton()) {
                return;
            }

            categoryDAO.deleteCategory(categoryId);

            // Refresh table and clear fields
            refreshTable();
            clearFields();
            showAlert("Thành công", "Xóa danh mục thành công!", Alert.AlertType.INFORMATION);

        } catch(NumberFormatException e) {
            showAlert("Lỗi", "ID danh mục phải là số!", Alert.AlertType.ERROR);
        } catch(Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Có lỗi xảy ra khi xóa danh mục: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void initialize() {
        setupTable();

        // Add table selection listener to populate fields when a row is selected
        categoryTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void setupTable() {
        catIdColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        catNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        catDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        refreshTable();
    }

    private void refreshTable() {
        categoryList = getCategoryList();
        categoryTable.setItems(categoryList);
    }

    private ObservableList<Category> getCategoryList() {
        return FXCollections.observableArrayList(categoryDAO.getAllCategories());
    }

    private void populateFields(Category category) {
        categoryIdField.setText(String.valueOf(category.getCategoryId()));
        categoryNameField.setText(category.getCategoryName());
        categoryDescField.setText(category.getDescription());
    }

    private void clearFields() {
        categoryIdField.clear();
        categoryNameField.clear();
        categoryDescField.clear();
        categoryTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}