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
        top5BestSellersObservableList = getTop5BestSellersObservableList(30);
    }
    public void setupType(int limit){
            importProductData(top5BestSellersObservableList, limit);
    }

        ObservableList<InventoryProduct> getTop5BestSellersObservableList(int limit) throws SQLException {
            // Get data for last 30 days (you can modify this period as needed)
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(30);

            List<Map<String, Object>> topProducts = reportDAO.getTopSellingProductsByRevenue(startDate, endDate, limit);
            if(topProducts.isEmpty()){
                throw new SQLException("No products found");
            }
            ObservableList<InventoryProduct> productList = FXCollections.observableArrayList();

            for (Map<String, Object> productData : topProducts) {
                InventoryProduct product = createInventoryProductFromMap(productData);
                productList.add(product);
            }

            return productList;
        }

    private InventoryProduct createInventoryProductFromMap(Map<String, Object> productData) throws SQLException {
        InventoryProduct product;
        product = inventoryProductDAO.getInventoryProductById((Integer) productData.get("productId"));
        return product;
    }

    void importProductData(ObservableList<InventoryProduct> product,int limit){
        int sizeProduct = (limit < product.size()) ? limit : product.size();
        System.out.println("sizeProduct: " + sizeProduct);
        ThreadUtil<VBox> threadImport = new ThreadUtil<>(5);
        for(int i = 0; i< sizeProduct; i++){
            InventoryProduct p = product.get(i);
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
