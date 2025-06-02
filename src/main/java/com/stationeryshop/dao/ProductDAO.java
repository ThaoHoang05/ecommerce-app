package com.stationeryshop.dao;

import com.stationeryshop.model.Category;
import com.stationeryshop.model.Product;
import com.stationeryshop.utils.DBConnection;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProductDAO {
    private DBConnection db;

    public ProductDAO() {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/db.properties");
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String useradmin = props.getProperty("db.admin");
        String pwdadmin = props.getProperty("db.adminpwd");
        if (useradmin == null || pwdadmin == null) {
            JOptionPane.showMessageDialog(null, "Database credentials not found in properties file", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.db = new DBConnection(useradmin, pwdadmin);
    }

    public ProductDAO(String useradmin, String pwdadmin) {
        this.db = new DBConnection(useradmin, pwdadmin);
    }

    public void addProduct(Product product) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO products (product_name, description, price, category_id, image_url, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
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
            System.out.println("Add product success");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE products SET product_name = ?, description = ?, price = ?, category_id = ?, image_url = ?, updated_at = ? " +
                     "WHERE product_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
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
            System.out.println("Update product success");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteProduct(int productId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "DELETE FROM products WHERE product_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            System.out.println("Delete product success");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Product getProductById(int productId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT p.*, c.category_id, c.category_name, c.description as category_desc " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.product_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Product> getProductsByName(String name) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM products WHERE LOWER(product_name) LIKE LOWER(?)";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + name + "%");
            rs = stmt.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            rs = stmt.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<Product> getAllProductsWithCategory() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT p.*, c.category_id, c.category_name, c.description as category_desc " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
