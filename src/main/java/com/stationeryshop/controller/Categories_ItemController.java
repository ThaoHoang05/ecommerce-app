package com.stationeryshop.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.stationeryshop.model.Category;

public class Categories_ItemController {
    @FXML
    private Label categoryNameLabel;

    public void setData(String item) {
        categoryNameLabel.setText(item);
    }
}