package com.stationeryshop.model;

import com.stationeryshop.chatbot.GeminiApiService;
import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.InvoiceDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.dao.SupplierDAO;

public class Chatbot {
    private final GeminiApiService geminiApiService;
    private final ProductDAO productDao;
    private final SupplierDAO supplierDao;
    private final CategoryDAO categoryDao;
    private final InventoryDAO inventoryDao;
    private final InvoiceDAO invoiceDao;

    public Chatbot(GeminiApiService geminiApiService, ProductDAO productDao, SupplierDAO supplierDao,
                   CategoryDAO categoryDao, InventoryDAO inventoryDao, InvoiceDAO invoiceDao) {
        this.geminiApiService = geminiApiService;
        this.productDao = productDao;
        this.supplierDao = supplierDao;
        this.categoryDao = categoryDao;
        this.inventoryDao = inventoryDao;
        this.invoiceDao = invoiceDao;
    }

    public String getResponse(String userMessage) {
        try {
            String prompt = "Trả lời bằng tiếng Việt: " + userMessage;
            return geminiApiService.askGemini(prompt);
        } catch (Exception e) {
            return "Sorry, I couldn't process your request: " + e.getMessage();
        }
    }
}
