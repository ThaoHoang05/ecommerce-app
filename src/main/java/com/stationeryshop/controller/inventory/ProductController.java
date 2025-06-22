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
    private InventoryProductController inventoryProductController;
    private InventoryCategoryController inventoryCategoryController;

    public void initialize() {
        tabPane.getSelectionModel().select(ProductTab);
        loadDefault();

        ProductTab.setOnSelectionChanged(event -> {
            if (ProductTab.isSelected() && isLoadedProduct) {
                if(isLoadedCategory){
                    System.out.println("Doan nay chay");
                    inventoryProductController.refreshDataBar(inventoryCategoryController.handleRefreshCategory());
                }
            }
        });
        CategoryTab.setOnSelectionChanged(event -> {
            if (CategoryTab.isSelected()&& !isLoadedCategory) {
                try {
                    FXMLLoader loader = (new FXMLLoader(getClass().getResource("/fxml/Inventory/InventoryCategoryTab.fxml")));
                    Parent content = loader.load();
                    CategoryTab.setContent(content);
                    inventoryCategoryController =  loader.getController();
                    isLoadedCategory = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            }
        );
        InventoryTab.setOnSelectionChanged(event -> {
            if (InventoryTab.isSelected() && !isLoadedInventory) {
                try {
                    Parent content = (new FXMLLoader(getClass().getResource("/fxml/Inventory/InventoryTab.fxml"))).load();
                    InventoryTab.setContent(content);
                    isLoadedInventory = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void loadDefault(){
        try {
            FXMLLoader loader = (new FXMLLoader(getClass().getResource("/fxml/Inventory/InventoryProductTab.fxml")));
            Parent content = loader.load(); //
            ProductTab.setContent(content);
            inventoryProductController = loader.getController();
            isLoadedProduct = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
