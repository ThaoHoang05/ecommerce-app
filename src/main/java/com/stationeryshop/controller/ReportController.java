package com.stationeryshop.controller;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.Category;
import com.stationeryshop.model.InventoryItem;
import com.stationeryshop.model.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


public class ReportController {

    @FXML
    private Button generateReportBtn;

    @FXML
    private TextField customerSearchField;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private BarChart<?, ?> revenueChart;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private PieChart productChart;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Button clearSearchBtn;

    @FXML
    private TableView<?> customerOrdersTable;

    @FXML
    private TableColumn<?, ?> orderIDColumn;

    @FXML
    private TableColumn<?, ?> customerPhoneColumn;

    @FXML
    private Label totalCustomersLabel;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button searchCustomerBtn;

    @FXML
    private Button exportReportBtn;

    @FXML
    private TableColumn<?, ?> customerNameColumn;

    @FXML
    private ComboBox<?> reportTypeComboBox;

    @FXML
    private TableColumn<?, ?> customerEmailColumn;

    @FXML
    private TableColumn<?, ?> SpentColumn;

    @FXML
    private TableColumn<?, ?> customerIdColumn;

    @FXML
    void handleGenerateReport(ActionEvent event) {

    }

    @FXML
    void handleExportReport(ActionEvent event) {

    }

    @FXML
    void handleSearchCustomer(ActionEvent event) {

    }

    @FXML
    void handleClearSearch(ActionEvent event) {

    }

}
