package com.stationeryshop.test;

import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.InventoryItem;
import com.stationeryshop.model.Product;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryDAOTest {

    private static Connection conn;
    private static InventoryDAO inventoryDAO;
    private static ProductDAO productDAO;
    private static int testProductId;

    @BeforeAll
    public static void init() throws SQLException {
        conn = util.DBConnection.getConnection();
        productDAO = new ProductDAO(conn);
        inventoryDAO = new InventoryDAO(conn);

        Product product = new Product(0, "InventoryTestProduct", "desc", 10.0, null,
                LocalDateTime.now(), LocalDateTime.now(), null);
        productDAO.addProduct(product);

        testProductId = productDAO.getProductsByName("InventoryTestProduct").get(0).getProductId();

        conn.createStatement().execute("INSERT INTO inventory (product_id, quantity_on_hand, last_stocked_date) VALUES (" +
                testProductId + ", 5, current_date)");
    }

    @Test
    public void testUpdateAndGetStock() throws SQLException {
        inventoryDAO.updateStock(testProductId, 3);
        int stock = inventoryDAO.getStockLevel(testProductId);
        assertEquals(8, stock);
    }

    @Test
    public void testLowStockQuery() throws SQLException {
        List<InventoryItem> lowStockItems = inventoryDAO.getLowStockItems(10);
        boolean found = lowStockItems.stream().anyMatch(i -> i.getProduct().getProductId() == testProductId);
        assertTrue(found);
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        conn.createStatement().execute("DELETE FROM inventory WHERE product_id = " + testProductId);
        conn.createStatement().execute("DELETE FROM products WHERE product_name = 'InventoryTestProduct'");
        conn.close();
    }
}
