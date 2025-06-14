package com.stationeryshop.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.stationeryshop.model.Supplier;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.DBConnection;
import com.stationeryshop.utils.Session;

import javax.swing.*;

public class SupplierDAO {
    private DBConnection dbConnection;

    // Khởi tạo với username và password cho DBConnection
    public SupplierDAO() {
        this.dbConnection = new DBConnection();
    }

    // Thêm nhà cung cấp
    public boolean addSupplier(Supplier supplier) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối tới cơ sở dữ liệu");
            }

            String sql = "INSERT INTO suppliers (supplier_name, contact_person, email, phone, address) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getPhone());
            stmt.setString(5, supplier.getAddress());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            stmt.close();
            conn.close();
        }
    }

    // Cập nhật nhà cung cấp
    public boolean updateSupplier(Supplier supplier) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối tới cơ sở dữ liệu");
            }

            String sql = "UPDATE suppliers SET supplier_name = ?, contact_person = ?, email = ?, phone = ?, address = ? WHERE supplier_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, supplier.getSupplierName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getEmail());
            stmt.setString(4, supplier.getPhone());
            stmt.setString(5, supplier.getAddress());
            stmt.setInt(6, supplier.getSupplierId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            stmt.close();
            conn.close();
        }
    }

    // Xóa nhà cung cấp
    public boolean deleteSupplier(int supplierId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối tới cơ sở dữ liệu");
            }

            String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, supplierId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            stmt.close();
            conn.close();
        }
    }

    // Lấy danh sách nhà cung cấp
    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối tới cơ sở dữ liệu");
            }

            String sql = "SELECT * FROM suppliers";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setContactPerson(rs.getString("contact_person"));
                supplier.setEmail(rs.getString("email"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setCreatedAt(rs.getTimestamp("created_at"));
                supplier.setUpdatedAt(rs.getTimestamp("updated_at"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
        return suppliers;
    }

    // Tìm nhà cung cấp theo ID
    public Supplier getSupplierById(int supplierId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối tới cơ sở dữ liệu");
            }

            String sql = "SELECT * FROM suppliers WHERE supplier_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, supplierId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setContactPerson(rs.getString("contact_person"));
                supplier.setEmail(rs.getString("email"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setCreatedAt(rs.getTimestamp("created_at"));
                supplier.setUpdatedAt(rs.getTimestamp("updated_at"));
                return supplier;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
        return null;
    }

    // Tìm nhà cung cấp theo tên
    public Supplier getSuppliersByName(String supplierName) throws SQLException {
        Supplier supplier = new Supplier();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối tới cơ sở dữ liệu");
            }

            String sql = "SELECT * FROM suppliers WHERE supplier_name LIKE ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + supplierName + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setContactPerson(rs.getString("contact_person"));
                supplier.setEmail(rs.getString("email"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setCreatedAt(rs.getTimestamp("created_at"));
                supplier.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
        return supplier;
    }

}