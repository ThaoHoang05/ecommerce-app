package com.stationeryshop.dao;

import com.stationeryshop.model.InventoryItem;
import com.stationeryshop.model.Product;

import com.stationeryshop.utils.DBConnection;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InventoryDAO {

    private DBConnection db;

    public InventoryDAO() {
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

    public InventoryDAO(String useradmin, String pwdadmin) {
        this.db = new DBConnection(useradmin, pwdadmin);
    }

    public void updateStock(int productId, int quantityChange) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "UPDATE inventory SET quantity_on_hand = quantity_on_hand + ?, last_stocked_date = ? WHERE product_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantityChange);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, productId);
            stmt.executeUpdate();
            System.out.println("Update stock success");
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

    public int getStockLevel(int productId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT quantity_on_hand FROM inventory WHERE product_id = ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity_on_hand");
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
        return 0;
    }

    public List<InventoryItem> getLowStockItems(int threshold) {
        List<InventoryItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT i.*, p.product_name FROM inventory i " +
                     "JOIN products p ON i.product_id = p.product_id " +
                     "WHERE quantity_on_hand <= ?";
        try {
            conn = db.connect();
            if (conn == null) JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, threshold);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));

                InventoryItem item = new InventoryItem(
                    rs.getInt("inventory_id"),
                    product,
                    rs.getInt("quantity_on_hand"),
                    rs.getDate("last_stocked_date").toLocalDate()
                );
                list.add(item);
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
