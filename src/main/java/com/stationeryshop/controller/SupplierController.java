package com.stationeryshop.controller;

import com.stationeryshop.dao.SupplierDAO;

import com.stationeryshop.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.util.List;

public class SupplierController {

    @FXML
    private TextField txtPhone;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TextField txtSupplierName;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private Button editBtn;

    @FXML
    private TableView<?> tableSuppliers;

    @FXML
    private TextField txtContactPerson;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button supplierSearchBtn;

    @FXML
    private TextField txtSupplierId;

    @FXML
    private TableColumn<?, ?> colContactPerson;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private Button addSupplierBtn;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    void handleTableClick(ActionEvent event) {

    }

    @FXML
    void handleEditSupplier(ActionEvent event) {

    }

    @FXML
    void handleDeleteSupplier(ActionEvent event) {

    }

    @FXML
    void handleSearch(ActionEvent event) {

    }

    @FXML
    void handleSearchSupplier(ActionEvent event) {

    }

    @FXML
    void handleAddSupplier(ActionEvent event) {

    }

    private SupplierDAO supplierDAO;

    public SupplierController() {
        supplierDAO = new SupplierDAO();
    }

    public boolean addSupplier(Supplier supplier) {
        return supplierDAO.addSupplier(supplier);
    }

    public boolean updateSupplier(Supplier supplier) {
        return supplierDAO.updateSupplier(supplier);
    }

    public boolean deleteSupplier(int supplierId) {
        return supplierDAO.deleteSupplier(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

}

