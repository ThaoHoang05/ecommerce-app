package com.stationeryshop.controller;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.model.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.util.ArrayList;

public class CategoriesController {

    @FXML
    private GridPane gridPane;

    private ArrayList<Category> categories;
    private CategoryDAO categoryDAO;

    ArrayList<Category> getCategories() {
        categoryDAO = new CategoryDAO();
        return (ArrayList<Category>) categoryDAO.getAllCategories();
    }

    public void initialize() {
        categories = getCategories();
        final String ITEM_FILE_PATH = "/fxml/Categories_Item.fxml";

        // Thiết lập constraints cho GridPane để đảm bảo kích thước cố định
        setupGridConstraints();

        int col = 0;
        int row = 0;
        final int MAX_ROWS = 1; // Giới hạn 2 dòng

        for(Category category : categories) {
            String categoryName = category.getCategoryName();
            System.out.println(categoryName);
            try {
                FXMLLoader fxmlloader = new FXMLLoader();
                fxmlloader.setLocation(getClass().getResource(ITEM_FILE_PATH));
                Categories_ItemController itemController = new Categories_ItemController(category);
                fxmlloader.setController(itemController);
                AnchorPane anchorPane = fxmlloader.load();
                itemController.setData();

                // Đảm bảo AnchorPane có kích thước cố định 100x100
                anchorPane.setPrefSize(100, 100);
                anchorPane.setMinSize(100, 100);
                anchorPane.setMaxSize(100, 100);

                // Thêm vào GridPane tại vị trí (col, row)
                gridPane.add(anchorPane, col, row);

                // Set margin nhỏ hơn để tối ưu không gian
                GridPane.setMargin(anchorPane, new Insets(20, 20, 20, 20));

                // Tính toán vị trí tiếp theo
                row++;
                if(row >= MAX_ROWS) {
                    row = 0;
                    col++;
                }

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupGridConstraints() {
        // Xóa constraints cũ nếu có
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        // Tính số cột cần thiết
        int totalCategories = categories != null ? categories.size() : 0;
        int numberOfColumns = (totalCategories + 1) / 2; // Làm tròn lên cho 2 rows

        // Thiết lập column constraints - mỗi cột rộng 110px (100px + margin)
        for (int i = 0; i < numberOfColumns; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(110);
            colConstraints.setMinWidth(110);
            colConstraints.setMaxWidth(110);
            gridPane.getColumnConstraints().add(colConstraints);
        }

        // Thiết lập row constraints - mỗi dòng cao 110px (100px + margin)
        for (int i = 0; i < 2; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(110);
            rowConstraints.setMinHeight(110);
            rowConstraints.setMaxHeight(110);
            gridPane.getRowConstraints().add(rowConstraints);
        }
    }
}