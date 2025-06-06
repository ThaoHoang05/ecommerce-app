package com.stationeryshop.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.stationeryshop.model.Category;

public class Categories_ItemController {
    @FXML
    private Label CategoryName;

    @FXML
    private VBox categoryCard;

    private Category item;

    public Categories_ItemController(Category item) {
        this.item = item;
    }

    void setData() {
        if (CategoryName != null && item != null) {
            CategoryName.setText(this.item.getCategoryName());

            // Thiết lập properties cho label để text hiển thị đầy đủ trong grid 100x100
            CategoryName.setMaxWidth(80); // Để lại space cho padding
            CategoryName.setPrefWidth(80);
            CategoryName.setWrapText(true);
            CategoryName.setStyle("-fx-text-alignment: center; -fx-alignment: center;");

            // Đảm bảo VBox có kích thước cố định
            if (categoryCard != null) {
                categoryCard.setPrefSize(100, 100);
                categoryCard.setMinSize(100, 100);
                categoryCard.setMaxSize(100, 100);
            }
        }
    }
}