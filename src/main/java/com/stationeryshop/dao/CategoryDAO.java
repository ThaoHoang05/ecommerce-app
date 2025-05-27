package com.stationeryshop.dao;

import com.stationeryshop.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO categories (category_name, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.executeUpdate();
        }
    }

    public void updateCategory(Category category) throws SQLException {
        String sql = "UPDATE categories SET category_name = ?, description = ? WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());
            stmt.executeUpdate();
        }
    }

    public void deleteCategory(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
        }
    }

    public Category getCategoryById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("description")
                );
            }
        }
        return null;
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("description")
                ));
            }
        }
        return list;
    }
}
