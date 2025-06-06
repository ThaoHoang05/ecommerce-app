package com.stationeryshop.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import com.stationeryshop.model.Category;

import java.io.IOException;


public class Categories_ItemController {
    @FXML
    private Label categoryNameLabel;

    private String item;

    private Pane parentContainer;

    @FXML
    void clickToViewCategoryProduct(MouseEvent event) throws IOException {

    }

    public void setData(String item) {
        this.item = item;
        categoryNameLabel.setText(item);
    }
}