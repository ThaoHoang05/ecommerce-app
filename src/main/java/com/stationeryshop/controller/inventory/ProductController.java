package com.stationeryshop.controller.inventory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class ProductController {
    @FXML
    private Tab CategoryTab;

    @FXML
    private Tab ProductTab;

    @FXML
    private Tab InventoryTab;

    @FXML
    private TabPane tabPane;

    private boolean isLoadedProduct = false;
    private boolean isLoadedInventory = false;
    private boolean isLoadedCategory = false;

    public void initialize() {
        tabPane.getSelectionModel().select(ProductTab);
        loadDefault();

        ProductTab.setOnSelectionChanged(event -> {
            if (ProductTab.isSelected() && !isLoadedProduct) {
                try {
                    Parent content = (new FXMLLoader(getClass().getResource("/fxml/Inventory/InventoryProductTab.fxml"))).load();
                    ProductTab.setContent(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        CategoryTab.setOnSelectionChanged(event -> {
            if (CategoryTab.isSelected() && !isLoadedInventory) {
                try {
                    Parent content = (new FXMLLoader(getClass().getResource("/fxml/Inventory/InventoryCategoryTab.fxml"))).load();
                    CategoryTab.setContent(content);
                    isLoadedInventory = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        InventoryTab.setOnSelectionChanged(event -> {
            if (InventoryTab.isSelected() && !isLoadedCategory) {
                try {
                    Parent content = (new FXMLLoader(getClass().getResource("/fxml/Inventory/InventoryTab.fxml"))).load();
                    InventoryTab.setContent(content);
                    isLoadedCategory = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void loadDefault(){
        try {
            Parent content = (new FXMLLoader(getClass().getResource("/fxml/Inventory/InventoryProductTab.fxml"))).load();
            ProductTab.setContent(content);
            isLoadedProduct = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
