package com.stationeryshop.model;

import java.time.LocalDate;

public class InventoryItem {
    private int inventoryId;
    private Product product;
    private int quantityOnHand;
    private LocalDate lastStockedDate;

    // Constructors
    public InventoryItem() {}

    public InventoryItem(int inventoryId, Product product, int quantityOnHand, LocalDate lastStockedDate) {
        this.inventoryId = inventoryId;
        this.product = product;
        this.quantityOnHand = quantityOnHand;
        this.lastStockedDate = lastStockedDate;
    }

    // Getters and Setters
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public LocalDate getLastStockedDate() {
        return lastStockedDate;
    }

    public void setLastStockedDate(LocalDate lastStockedDate) {
        this.lastStockedDate = lastStockedDate;
    }
}
