package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.model.InventoryItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class InventoryController {

    @FXML
    private TableView<InventoryItem> inventoryTable;

    @FXML
    private TableColumn<InventoryItem, String> invLastStockedColumn;

    @FXML
    private TableColumn<InventoryItem, Integer> invQuantityColumn;

    @FXML
    private TableColumn<InventoryItem, String> invProductColumn;

    @FXML
    private TableColumn<InventoryItem, Integer> invIdColumn;

    private ObservableList<InventoryItem> inventoryList;

    private InventoryDAO inventory;

    public void initialize(){
        //setTable
        setTableInventory();
    }
    private void setTableInventory(){
        invLastStockedColumn.setCellValueFactory(new PropertyValueFactory<>("lastStocked"));
        invQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        invProductColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        invIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //inventoryList = FXCollections.observableArrayList(inventory.getLowStockItems(Integer.parseInt()));
    }
}
