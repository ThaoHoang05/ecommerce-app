package com.stationeryshop.test;

import com.stationeryshop.dao.InventoryProductDAO;

public class TestLoadingInventoryProduct {
    public static void main(String[] args) {
        InventoryProductDAO ip = new InventoryProductDAO();
        long startTime = System.nanoTime();
        ip.getAllInventoryProduct();
        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime)/1_000_000;
        System.out.println(elapsedTime);
    }
}
