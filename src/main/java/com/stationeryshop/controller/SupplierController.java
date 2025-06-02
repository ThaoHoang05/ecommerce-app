package com.stationeryshop.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.stationeryshop.dao.SupplierDAO;
import com.stationeryshop.model.Supplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SupplierController implements Initializable {
    @FXML
    private TextField txtPhone;

    @FXML
    private TableColumn<Supplier, String> colName;

    @FXML
    private TextField txtSupplierName;

    @FXML
    private TableColumn<Supplier, String> colPhone;

    @FXML
    private Button editBtn;

    @FXML
    private TableView<Supplier> tableSuppliers;

    @FXML
    private TextField txtContactPerson;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button supplierSearchBtn;

    @FXML
    private TextField txtSupplierId;

    @FXML
    private TableColumn<Supplier, String> colContactPerson;

    @FXML
    private TableColumn<Supplier, String> colEmail;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<Supplier, String> colAddress;

    @FXML
    private Button addSupplierBtn;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableColumn<Supplier, Integer> colId;

    private SupplierDAO supplierDAO;
    private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();

    public SupplierController() {
        supplierDAO = new SupplierDAO();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colContactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadSuppliers();
    }

    private void loadSuppliers() {
        supplierList.clear();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        supplierList.addAll(suppliers);
        tableSuppliers.setItems(supplierList);
    }

    @FXML
    void handleTableClick(javafx.scene.input.MouseEvent event) {
        Supplier selected = tableSuppliers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtSupplierId.setText(String.valueOf(selected.getSupplierId()));
            txtSupplierName.setText(selected.getSupplierName());
            txtContactPerson.setText(selected.getContactPerson());
            txtEmail.setText(selected.getEmail());
            txtPhone.setText(selected.getPhone());
            txtAddress.setText(selected.getAddress());
        }
    }

    @FXML
    void handleEditSupplier(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtSupplierId.getText());
            Supplier supplier = new Supplier();
            supplier.setSupplierId(id);
            supplier.setSupplierName(txtSupplierName.getText());
            supplier.setContactPerson(txtContactPerson.getText());
            supplier.setEmail(txtEmail.getText());
            supplier.setPhone(txtPhone.getText());
            supplier.setAddress(txtAddress.getText());
            if (supplierDAO.updateSupplier(supplier)) {
                loadSuppliers();
                showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn nhà cung cấp để sửa!");
        }
    }

    @FXML
    void handleDeleteSupplier(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtSupplierId.getText());
            if (supplierDAO.deleteSupplier(id)) {
                loadSuppliers();
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Xóa thành công!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Xóa thất bại!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn nhà cung cấp để xóa!");
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadSuppliers();
        } else {
            List<Supplier> suppliers = supplierDAO.getSuppliersByName(keyword);
            supplierList.setAll(suppliers);
            tableSuppliers.setItems(supplierList);
        }
    }

    @FXML
    void handleSearchSupplier(ActionEvent event) {
        handleSearch(event);
    }

    @FXML
    void handleAddSupplier(ActionEvent event) {
        Supplier supplier = new Supplier();
        supplier.setSupplierName(txtSupplierName.getText());
        supplier.setContactPerson(txtContactPerson.getText());
        supplier.setEmail(txtEmail.getText());
        supplier.setPhone(txtPhone.getText());
        supplier.setAddress(txtAddress.getText());
        if (supplierDAO.addSupplier(supplier)) {
            loadSuppliers();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Thêm thành công!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm thất bại!");
        }
    }

    private void clearForm() {
        txtSupplierId.clear();
        txtSupplierName.clear();
        txtContactPerson.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

