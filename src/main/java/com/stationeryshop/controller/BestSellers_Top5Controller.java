package com.stationeryshop.controller;

import com.stationeryshop.dao.InventoryProductDAO;
import com.stationeryshop.dao.ReportDAO;
import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.utils.ThreadUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class BestSellers_Top5Controller {

    @FXML
    private HBox top5BestSellersHBox;

    private ObservableList<InventoryProduct> top5BestSellersObservableList = FXCollections.observableArrayList();

    private ReportDAO reportDAO = new ReportDAO();
    private InventoryProductDAO inventoryProductDAO = new InventoryProductDAO();

    public void initialize() throws SQLException {
        top5BestSellersObservableList = getTop5BestSellersObservableList();
        importProductData(top5BestSellersObservableList);
    }

        ObservableList<InventoryProduct> getTop5BestSellersObservableList() throws SQLException {
            // Get data for last 30 days (you can modify this period as needed)
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(30);

            List<Map<String, Object>> topProducts = reportDAO.getTopSellingProducts(startDate, endDate, 5);

            ObservableList<InventoryProduct> productList = FXCollections.observableArrayList();

            for (Map<String, Object> productData : topProducts) {
                InventoryProduct product = createInventoryProductFromMap(productData);
                productList.add(product);
            }

            return productList;
        }

    private InventoryProduct createInventoryProductFromMap(Map<String, Object> productData) {
        InventoryProduct product = new InventoryProduct();
        inventoryProductDAO.getInventoryProductById((Integer) productData.get("productId"));
        return product;
    }

    void importProductData(ObservableList<InventoryProduct> product){
        ThreadUtil<VBox> threadImport = new ThreadUtil<>(5);
        for(InventoryProduct p : product){
            Future<VBox> pVBox = (threadImport.executeTask(()->{
                try {
                    return setDataItem(p);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new VBox(); // Trả về VBox rỗng nếu có lỗi
                }
            }));
            try{
                VBox vBox = pVBox.get();
                top5BestSellersHBox.getChildren().add(vBox);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        threadImport.shutdown();
    }VBox setDataItem(InventoryProduct p) throws IOException {
        final String ITEM_PATH = "/fxml/ShopView_Item.fxml";
        System.out.println(getClass().getResource(ITEM_PATH));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ITEM_PATH));
        ShopView_ItemController controller = new ShopView_ItemController(p);
        loader.setController(controller);
        VBox vBox = loader.load();
        controller.setData();
        return vBox;
    }
}
