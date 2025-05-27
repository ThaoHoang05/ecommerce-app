package com.stationeryshop.test;

import com.stationeryshop.dao.CategoryDAO;
import com.stationeryshop.model.Category;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDAOTest {

    private static Connection conn;
    private static CategoryDAO categoryDAO;

    @BeforeAll
    public static void init() throws SQLException {
        conn = util.DBConnection.getConnection();
        categoryDAO = new CategoryDAO(conn);
    }

    @Test
    public void testAddAndGetCategory() throws SQLException {
        Category category = new Category(0, "Test Category", "Description");
        categoryDAO.addCategory(category);

        List<Category> categories = categoryDAO.getAllCategories();
        boolean found = categories.stream().anyMatch(c -> c.getCategoryName().equals("Test Category"));
        assertTrue(found);
    }

    @Test
    public void testUpdateCategory() throws SQLException {
        Category category = categoryDAO.getAllCategories().get(0);
        category.setDescription("Updated description");
        categoryDAO.updateCategory(category);

        Category updated = categoryDAO.getCategoryById(category.getCategoryId());
        assertEquals("Updated description", updated.getDescription());
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        conn.createStatement().execute("DELETE FROM categories WHERE category_name = 'Test Category'");
        conn.close();
    }
}
