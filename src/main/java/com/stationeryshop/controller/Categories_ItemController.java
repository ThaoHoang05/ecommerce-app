package com.stationeryshop.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import com.stationeryshop.model.Category;

import java.io.IOException;
import java.util.function.Consumer;


public class Categories_ItemController {
    @FXML
    private Label categoryNameLabel;

    private String item;

    private Consumer<String> onItemClicked;

    public void setOnItemClicked(Consumer<String> handler) {
        this.onItemClicked = handler;
    }

    @FXML
    void clickToViewCategoryProduct(MouseEvent event) throws IOException {
            if (onItemClicked != null) {
                System.out.println(item);
                onItemClicked.accept(item);
            }
    }

    public void setData(String item) {
        this.item = item;
        categoryNameLabel.setText(item);
    }
}