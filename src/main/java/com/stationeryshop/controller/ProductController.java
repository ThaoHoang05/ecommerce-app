
package com.stationery.controller;

import com.stationery.dao.CategoryDAO;
import com.stationery.dao.InventoryDAO;
import com.stationery.dao.ProductDAO;
import com.stationery.model.Category;
import com.stationery.model.InventoryItem;
import com.stationery.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductController {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final InventoryDAO inventoryDAO;

    public ProductController(Connection conn) {
        this.productDAO = new ProductDAO(conn);
        this.categoryDAO = new CategoryDAO(conn);
        this.inventoryDAO = new InventoryDAO(conn);
    }

    // ================= PRODUCT =================
    public void addProduct(Product product, int initialStock) throws SQLException {
        productDAO.addProduct(product);
        int productId = productDAO.getProductsByName(product.getProductName())
                .stream().filter(p -> p.getCreatedAt().equals(product.getCreatedAt()))
                .findFirst().orElseThrow(() -> new SQLException("Không tìm thấy sản phẩm vừa thêm")).getProductId();
        inventoryDAO.updateStock(productId, initialStock); // thêm tồn kho ban đầu
    }

    public void updateProduct(Product product) throws SQLException {
        productDAO.updateProduct(product);
    }

    public void deleteProduct(int productId) throws SQLException {
        // Có thể kiểm tra ràng buộc trước khi xóa hoặc dùng "soft delete"
        inventoryDAO.updateStock(productId, -inventoryDAO.getStockLevel(productId)); // optional: xóa tồn kho
        productDAO.deleteProduct(productId);
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProductsWithCategory();
    }

    public List<Product> getProductsByCategory(int categoryId) throws SQLException {
        return productDAO.getProductsByCategory(categoryId);
    }

    public List<Product> searchProducts(String keyword) throws SQLException {
        return productDAO.getProductsByName(keyword);
    }

    // ================= CATEGORY =================
    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }

    public void addCategory(Category category) throws SQLException {
        categoryDAO.addCategory(category);
    }

    public void updateCategory(Category category) throws SQLException {
        categoryDAO.updateCategory(category);
    }

    // ================= INVENTORY =================
    public int getProductStock(int productId) throws SQLException {
        return inventoryDAO.getStockLevel(productId);
    }

    public void updateStock(int productId, int quantityChange) throws SQLException {
        inventoryDAO.updateStock(productId, quantityChange);
    }
}
