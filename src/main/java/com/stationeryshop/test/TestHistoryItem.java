package com.stationeryshop.test;

import com.stationeryshop.controller.History_Order_ItemController;
import com.stationeryshop.dao.InvoiceDAO;
import com.stationeryshop.dao.UserDAO;
import com.stationeryshop.model.Invoice;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.Session;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class TestHistoryItem extends Application{
    static Invoice currentInvoice;
    public void start(Stage primaryStage) throws Exception {
        final String LOGIN_FXML_PATH = "/fxml/History_Order_Item.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_FXML_PATH));
        Parent root = loader.load();
        History_Order_ItemController controller = loader.getController();
        controller.setInvoiceData(currentInvoice);
        primaryStage.setTitle("History Item");
        primaryStage.setScene(new Scene(root, 550, 800));
        primaryStage.show();
    }
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        User cur = userDAO.getUser("customer2");
        Session.setCurrentUser(cur);
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        List<Invoice> list = invoiceDAO.getInvoicesByUserId(cur.getUser_id());
        currentInvoice = list.get(0);
        launch(args);
    }
}
