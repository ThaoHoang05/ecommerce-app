package com.stationeryshop.controller;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;

public class CategoriesController {
    @FXML
    private HBox categoriesPane;

    private ObservableList<Category> categories;
    private CategoryDAO categoryDAO;

    ObservableList<Category> getCategories() {
        categoryDAO = new CategoryDAO();
        categories = FXCollections.observableArrayList(categoryDAO.getAllCategories());
        return categories;
    }

    public void initialize() {
        categories = getCategories();
        categoriesPane.setSpacing(15);

        for(Category category : categories) {
            try{
                System.out.println(category.getCategoryName());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Categories_Item.fxml"));
                VBox categoryItem = fxmlLoader.load();
                Categories_ItemController controller = fxmlLoader.getController(); // Lấy controller từ FXML
                controller.setData(category.getCategoryName());
                categoriesPane.getChildren().add(categoryItem);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}