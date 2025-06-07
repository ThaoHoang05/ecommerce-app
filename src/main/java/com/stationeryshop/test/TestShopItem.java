package com.stationeryshop.test;

import com.stationeryshop.controller.ShopView_ItemController;
import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.model.Category;
import com.stationeryshop.model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TestShopItem extends Application{
    public static InventoryProduct product;
    public void start(Stage primaryStage) throws Exception {
        final String CATEGORY = "/fxml/ShopView_Item.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CATEGORY));
        ShopView_ItemController controller = new ShopView_ItemController(product);
        loader.setController(controller);// Đặt controller trước khi load
        Parent root = loader.load();
        controller.setData();// Bây giờ nó sẽ dùng instance đã có
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Test Shop View Item");
        primaryStage.show();
    }
    public static void main(String[] args) throws SQLException {
        product = new InventoryProduct();
        product.setCategory(1,"food", "nothing");
        product.setProduct(1,"test", "des", 0.0, null, "food");
        product.setSupplier("Nha cung cap 2");
        product.setInventoryItem(1);

        launch(args);
    }
}
