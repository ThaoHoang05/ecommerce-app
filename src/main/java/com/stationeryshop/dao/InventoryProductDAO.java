package com.stationeryshop.dao;


import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.time.LocalDate;

public class InventoryProductDAO {
    private DBConnection db;
    public InventoryProductDAO() {
        Properties props = new Properties();
        try{
            FileInputStream fis = new FileInputStream("src/main/resources/db.properties");
            props.load(fis);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        String useradmin = props.getProperty("db.admin");
        String pwdadmin = props.getProperty("db.adminpwd");
        this.db = new DBConnection(useradmin, pwdadmin);
    }

    public void addProduct(String name, String description, float price, int quantity, String category, String image, String supplier_name, float supply_price) {
        Connection conn = db.connect();
        PreparedStatement ps = null;

        String sql = "INSERT INTO inventoryProduct (product_name, description, price, category_id, image_url, supplier_id, supply_price, quantity_on_hand, last_stocked_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int category_id = findCategoryByName(category);
        int supplier_id = findSupplierByName(supplier_name);

        try {
            if (category_id == -1 || supplier_id == -1) {
                throw new Exception("Không tìm thấy danh mục hoặc nhà cung cấp.");
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setFloat(3, price);
            ps.setInt(4, category_id);
            ps.setString(5, image);
            ps.setInt(6, supplier_id);
            ps.setFloat(7, supply_price);
            ps.setInt(8, quantity);
            ps.setDate(9, java.sql.Date.valueOf(LocalDate.now()));

            int affectedRows = ps.executeUpdate();
            System.out.println("Product added successfully. Rows affected: " + affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    int findSupplierByName(String name_supplier){
        int id = -1;
        Connection conn = db.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select supplier_id from suppliers where supplier_name like ?";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, name_supplier);
            rs = ps.executeQuery();
            if(rs.next()){
                id = rs.getInt("supplier_id");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            conn = null;
            ps = null;
            rs = null;
        }
        return id;
    }

    int findCategoryByName(String category_name){
        int id = -1;
        Connection conn = db.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select category_id from categories where category_name like ?";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, category_name);
            rs = ps.executeQuery();
            if(rs.next()){
                id = rs.getInt("category_id");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            conn = null;
            ps = null;
            rs = null;
        }
        return id;
    }

    public void deleteProduct(int productId) {
        Connection conn = db.connect();
        PreparedStatement ps = null;

        // Xóa sản phẩm từ inventoryProduct → Trigger sẽ tự động xóa từ các bảng liên quan
        String sql = "DELETE FROM inventoryProduct WHERE product_id = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);

            int affectedRows = ps.executeUpdate();
            System.out.println("Product deleted successfully. Rows affected: " + affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void updateProduct(int productId, String name, String description, float price, int quantity, String category, String image, String supplier_name, float supply_price) {
        Connection conn = db.connect();
        PreparedStatement ps = null;

        // Cập nhật từ inventoryProduct → Trigger sẽ tự động cập nhật vào các bảng liên quan
        String sql = "UPDATE inventoryProduct SET product_name = ?, description = ?, price = ?, category_id = ?, image_url = ?, " +
                "supplier_id = ?, supply_price = ?, quantity_on_hand = ?, last_stocked_date = ? WHERE product_id = ?";

        int category_id = findCategoryByName(category);
        int supplier_id = findSupplierByName(supplier_name);

        try {
            if (category_id == -1 || supplier_id == -1) {
                throw new Exception("Không tìm thấy danh mục hoặc nhà cung cấp.");
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setFloat(3, price);
            ps.setInt(4, category_id);
            ps.setString(5, image);
            ps.setInt(6, supplier_id);
            ps.setFloat(7, supply_price);
            ps.setInt(8, quantity);
            ps.setDate(9, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.setInt(10, productId);

            int affectedRows = ps.executeUpdate();
            System.out.println("Product updated successfully. Rows affected: " + affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ObservableList<InventoryProduct> getAllInventoryProduct() {
        ObservableList<InventoryProduct> inventoryList = FXCollections.observableArrayList();
        Connection conn = db.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT ip.product_id ,ip.product_name, ip.description, ip.price, " +
                "ip.quantity_on_hand, ip.image_url, " +
                "c. category_id , c.category_name, c.description, " +
                "s.supplier_name " +
                "FROM " +
                "inventoryProduct ip " +
                "JOIN categories c ON ip.category_id = c.category_id " +
                "JOIN suppliers s ON ip.supplier_id = s.supplier_id " +
                "ORDER BY ip.product_name";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                InventoryProduct inventoryProduct = new InventoryProduct();

                // Set category information
                inventoryProduct.setCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description")
                );

                // Set product information
                inventoryProduct.setProduct(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("image_url"),
                        rs.getString("category_name")
                );

                // Set supplier information
                inventoryProduct.setSupplier(rs.getString("supplier_name"));

                // Set inventory item information
                inventoryProduct.setInventoryItem(rs.getInt("product_id"));

                inventoryList.add(inventoryProduct);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return inventoryList;
    }
}
