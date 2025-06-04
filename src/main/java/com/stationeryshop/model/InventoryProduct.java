package com.stationeryshop.model;

import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.SupplierDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryProduct {

    // su dung composition
    private Category category;
    private Product product;
    private List<Supplier> supplier;
    private InventoryItem inventoryItem;

    public void setCategory(int category_id,String category_name, String category_description) {
        this.category = new Category(category_id, category_name, category_description);
    }
    public void setProduct(int productId,String productName, String description, double price,
                           String imageUrl, String category) {
        this.product = new Product(productId, productName, description, price, imageUrl, this.category);
    }
    public void setSupplier(String supplier) throws SQLException {
        SupplierDAO supply = new SupplierDAO();
        this.supplier = supply.getSuppliersByName(supplier);
    }
    public void setInventoryItem(int product_id) {
        InventoryDAO inventory = new InventoryDAO();
        this.inventoryItem = inventory.getInventoryByProductId(product_id);
    }
    public int getProductId(){
        return this.product.getProductId();
    }
    public String getProductName(){
        return this.product.getProductName();
    }
    public String getProductDescription(){
        return this.product.getDescription();
    }
    public double getProductPrice(){
        return this.product.getPrice();
    }
    public int getQuantity(){
        return this.inventoryItem.getQuantityOnHand();
    }
    public String getCategoryName(){
        return this.category.getCategoryName();
    }
    public List<String> getSupplierName(){
        List<String> supplierName = new ArrayList<>();
        for(Supplier supplier : this.supplier){
            supplierName.add(supplier.getSupplierName());
        }
        return supplierName;
    }
}
