package com.stationeryshop.dao;

import com.stationeryshop.model.Category;

import com.stationeryshop.utils.DBConnection;
import java.io.FileInputStream;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;


public class CategoryDAO {

    private DBConnection db;

    public CategoryDAO() {
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

    public CategoryDAO(String useradmin, String pwdadmin) {
        this.db = new DBConnection(useradmin, pwdadmin);
    }

    public void addCategory(Category category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO categories (category_name, description) VALUES (?, ?)";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.executeUpdate();
            System.out.println("Add category success");
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

    public void updateCategory(Category category) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE categories SET category_name = ?, description = ? WHERE category_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());
            stmt.executeUpdate();
            System.out.println("Update category success");
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

    public void deleteCategory(int categoryId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
            System.out.println("Delete category success");
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

    public Category getCategoryById(int categoryId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("description")
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

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM categories";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("description")
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
}
