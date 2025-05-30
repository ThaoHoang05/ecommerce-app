package com.stationeryshop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class HistoryController {

    @FXML
    private ScrollPane scrollOrders;

    @FXML
    private Label lblOrderCount;

    @FXML
    private VBox odersListBox;

    @FXML
    private Label totalOrdersCountLbl;

    @FXML
    private Label totalSpentLbl;

}
