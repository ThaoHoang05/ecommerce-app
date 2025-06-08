package com.stationeryshop.controller;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class CategoriesController {
    @FXML
    private HBox categoriesPane;

    private Consumer<String> itemSelectedHandler;

    private ObservableList<Category> categories;
    private CategoryDAO categoryDAO;

    ObservableList<Category> getCategories() {
        categoryDAO = new CategoryDAO();
        categories = FXCollections.observableArrayList(categoryDAO.getAllCategories());
        return categories;
    }

    public void setItemSelectedHandler(Consumer<String> handler) {
        this.itemSelectedHandler = handler;
    }

    private void handleItemClicked(String category) {
        if (itemSelectedHandler != null) {
            itemSelectedHandler.accept(category);
        }
    }

    public void initialize() {
        categories = getCategories();
        categoriesPane.setSpacing(15);

        for(Category category : categories) {
            try{
                System.out.println(category.getCategoryName());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Categories_Item.fxml"));
                VBox categoryItem = fxmlLoader.load();
                Categories_ItemController controller = fxmlLoader.getController();
                // Lấy controller từ FXML
                controller.setData(category.getCategoryName());
                controller.setOnItemClicked(this::handleItemClicked);
                categoriesPane.getChildren().add(categoryItem);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}