package com.stationeryshop.dao;

import com.stationeryshop.model.Category;
import com.stationeryshop.model.Product;
import com.stationeryshop.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection conn;

    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (product_name, description, price, category_id, image_url, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            if (product.getCategory() != null)
                stmt.setInt(4, product.getCategory().getCategoryId());
            else
                stmt.setNull(4, Types.INTEGER);
            stmt.setString(5, product.getImageUrl());
            stmt.setTimestamp(6, Timestamp.valueOf(product.getCreatedAt()));
            stmt.setTimestamp(7, Timestamp.valueOf(product.getUpdatedAt()));
            stmt.executeUpdate();
        }
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET product_name = ?, description = ?, price = ?, category_id = ?, image_url = ?, updated_at = ? " +
                     "WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            if (product.getCategory() != null)
                stmt.setInt(4, product.getCategory().getCategoryId());
            else
                stmt.setNull(4, Types.INTEGER);
            stmt.setString(5, product.getImageUrl());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(7, product.getProductId());
            stmt.executeUpdate();
        }
    }

    public void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }

    public Product getProductById(int productId) throws SQLException {
        String sql = "SELECT p.*, c.category_id, c.category_name, c.description as category_desc " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category category = new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("category_desc")
                );
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("image_url"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    category
                );
            }
        }
        return null;
    }

    public List<Product> getProductsByName(String name) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(product_name) LIKE LOWER(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("image_url"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    null
                ));
            }
        }
        return list;
    }

    public List<Product> getProductsByCategory(int categoryId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("image_url"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    null
                ));
            }
        }
        return list;
    }

    public List<Product> getAllProductsWithCategory() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.category_id, c.category_name, c.description as category_desc " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category category = new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("category_desc")
                );
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("image_url"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    category
                );
                list.add(product);
            }
        }
        return list;
    }
}
