package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.InventoryItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class InventoryController {

    @FXML
    private TableView<InventoryItem> inventoryTable;

    @FXML
    private TableColumn<InventoryItem, LocalDate> invLastStockedColumn;

    @FXML
    private TableColumn<InventoryItem, Integer> invQuantityColumn;

    @FXML
    private TableColumn<InventoryItem, String> invProductColumn;

    @FXML
    private TableColumn<InventoryItem, Integer> invIdColumn;

    private ObservableList<InventoryItem> inventoryList;

    private ProductDAO productDAO;
    private InventoryDAO inventoryDAO = new InventoryDAO();

    public void initialize(){
        // Set up table columns
        setTableInventory();
        // Load data into table
        loadInventoryData();
    }

    private void setTableInventory(){
        // Set up cell value factories for each column
        invIdColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        invProductColumn.setCellValueFactory(cellData -> {
            // Get product name from the product object
            String productName = cellData.getValue().getProduct().getProductName();
            return new javafx.beans.property.SimpleStringProperty(productName);
        });
        invQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));
        invLastStockedColumn.setCellValueFactory(new PropertyValueFactory<>("lastStockedDate"));
    }

    private void loadInventoryData(){
        // Load inventory data from database
        inventoryList = inventoryDAO.getAllInventoryItems();
        inventoryTable.setItems(inventoryList);
    }

    // Method to refresh the table data
    public void refreshTable(){
        loadInventoryData();
    }

    // Method to get selected inventory item
    public InventoryItem getSelectedInventoryItem(){
        return inventoryTable.getSelectionModel().getSelectedItem();
    }

    // Method to update table after data changes
    public void updateTable(){
        inventoryTable.refresh();
    }
}