package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.CategoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private CategoryDAO category = new CategoryDAO();

    @FXML
    void handleAddCategory(ActionEvent event) {
        try {
            category.addCategory(new Category(Integer.parseInt(categoryIdField.getText()),categoryNameField.getText(), categoryIdField.getText()));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateCategory(ActionEvent event) {
        try {
            category.updateCategory(new Category(Integer.parseInt(categoryIdField.getText()),categoryNameField.getText(), categoryIdField.getText()));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteCategory(ActionEvent event) {
        try{
            category.deleteCategory(Integer.parseInt(categoryIdField.getText()));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void initialize(){
        //setup table
        setupTable();
    }
    private void setupTable(){
        catIdColumn.setCellValueFactory(new PropertyValueFactory<>("catId"));
        catNameColumn.setCellValueFactory(new PropertyValueFactory<>("catName"));
        catDescColumn.setCellValueFactory(new PropertyValueFactory<>("catDesc"));
        categoryList = getCategoryList();
        categoryTable.setItems(categoryList);
    }
    private ObservableList<Category> getCategoryList(){
        return FXCollections.observableArrayList(category.getAllCategories());
    }
}
