package com.stationeryshop.controller;

import com.stationeryshop.dao.InventoryProductDAO;
import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.utils.ThreadUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ShopViewController {
    @FXML
    private Label CategoryLbl;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane productsScrollPane;

    @FXML
    private FlowPane productsFlowPane;

    private ObservableList<InventoryProduct> products;

    private String categoryFilter;

    private Consumer<InventoryProduct> viewDetailsItem;

    public void setItemSelectedHandler(Consumer<InventoryProduct> handler) {
        this.viewDetailsItem = handler;
    }

    public void initialize(){
        products = getProducts();
    }

    void setup(String category){
        this.categoryFilter = category;
        if(categoryFilter != null){
            System.out.println(categoryFilter);
            CategoryLbl.setText(categoryFilter);
            handleCategoryFilter(categoryFilter);
        }else{
        importProductData(products);
    }}
    // Lay product tu csdl
    ObservableList<InventoryProduct> getProducts() {
        InventoryProductDAO inventory = new InventoryProductDAO();
        return FXCollections.observableArrayList(inventory.getAllInventoryProduct());
    }


    // Xu ly hien ra 10 san pham
    void importProductData(ObservableList<InventoryProduct> product){
        ThreadUtil<VBox> threadImport = new ThreadUtil<>(5);
        for(InventoryProduct p : product){
            System.out.println(p.getCategoryName());
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
                productsFlowPane.getChildren().add(vBox);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        threadImport.shutdown();
    }

    VBox setDataItem(InventoryProduct p) throws IOException {
        final String ITEM_PATH = "/fxml/ShopView_Item.fxml";
        System.out.println(getClass().getResource(ITEM_PATH));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ITEM_PATH));
        ShopView_ItemController controller = new ShopView_ItemController(p);
        controller.setOnItemClicked(this::handleItemClicked);
        loader.setController(controller);
        VBox vBox = loader.load();
        controller.setData();
        return vBox;
    }
    @FXML
    void searchOnPressed(ActionEvent event) {
        String searchTerm = searchField.getText().trim().toLowerCase();
        handleFiltered(searchTerm);
    }

    void handleFiltered(String searchTerm){
        if (searchTerm.isEmpty()) {
            clearFilter();
            importProductData(products);
        }else{
            clearFilter();
            ObservableList<InventoryProduct> filteredList = products.filtered(product ->
                    product.getProductName().toLowerCase().contains(searchTerm) ||
                            product.getProductDescription().toLowerCase().contains(searchTerm) ||
                            product.getCategoryName().toLowerCase().contains(searchTerm)
            );
            if(filteredList == null){
                System.out.println("Nothing to search");
            }
            importProductData(filteredList);

        }
    }

    void handleCategoryFilter(String categoryName) {
        clearFilter();
        ObservableList<InventoryProduct> filteredList = products.filtered(product ->
                product.getCategoryName().equalsIgnoreCase(categoryName.trim())
        );
        products = filteredList;
        importProductData(products);
    }


    @FXML
    void sortByChoose(ActionEvent event) {
        clearFilter();
        setComboBoxSort();
    }

    void setComboBoxSort(){
        int type = sortComboBox.getSelectionModel().getSelectedIndex();
        sorted(products,type);
    }

    void sorted(ObservableList<InventoryProduct> product, int type) {
        try{
            ObservableList<InventoryProduct> sortableList;

            // Nếu là FilteredList, tạo ObservableList mới
            if (product instanceof javafx.collections.transformation.FilteredList) {
                sortableList = FXCollections.observableArrayList(product);
            } else {
                sortableList = product;
            }
        switch(type){
            case 0:
                sortableList.sort(Comparator.comparing(InventoryProduct::getProductPrice));
                break;
            case 1:
                sortableList.sort(Comparator.comparing(InventoryProduct::getProductPrice).reversed());
                break;
            case 2:
                sortableList.sort(Comparator.comparing(InventoryProduct::getProductName));
                break;
            case 3:
                sortableList.sort(Comparator.comparing(InventoryProduct::getProductName).reversed());
                break;
            default:
        }
            importProductData(sortableList);
        }
        catch(UnsupportedOperationException  e){
            e.printStackTrace();
        }
    }

    private void handleItemClicked(InventoryProduct product) {
        if (viewDetailsItem != null) {
            viewDetailsItem.accept(product);
        }
    }

        void clearFilter () {
            productsFlowPane.getChildren().clear();
        }

    }