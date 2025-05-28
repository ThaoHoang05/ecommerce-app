package com.stationeryshop.test;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.Category;
import com.stationeryshop.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.stationeryshop.utils.DBConnection;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProductDAOTest {

    private static Connection conn;
    private static ProductDAO productDAO;
    private static CategoryDAO categoryDAO;
    private static Category testCategory;
    private static DBConnection db = new DBConnection();

    @BeforeAll
    public static void init() throws SQLException {
        conn
        productDAO = new ProductDAO(conn);
        categoryDAO = new CategoryDAO(conn);
        testCategory = new Category(0, "ProductTestCategory", "Desc");
        categoryDAO.addCategory(testCategory);
        testCategory = categoryDAO.getAllCategories().stream()
                .filter(c -> c.getCategoryName().equals("ProductTestCategory"))
                .findFirst().orElse(null);
    }

    @Test
    public void testAddAndGetProduct() throws SQLException {
        Product product = new Product(0, "Test Product", "desc", 123.45, null,
                LocalDateTime.now(), LocalDateTime.now(), testCategory);
        productDAO.addProduct(product);

        List<Product> products = productDAO.getProductsByName("Test Product");
        assertFalse(products.isEmpty());
        assertEquals("Test Product", products.get(0).getProductName());
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        conn.createStatement().execute("DELETE FROM products WHERE product_name = 'Test Product'");
        conn.createStatement().execute("DELETE FROM categories WHERE category_name = 'ProductTestCategory'");
        conn.close();
    }
}
