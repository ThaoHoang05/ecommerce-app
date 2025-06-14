package com.stationeryshop.dao;

import com.stationeryshop.model.Invoice;
import com.stationeryshop.model.InvoiceDetail;
import com.stationeryshop.model.User;
import com.stationeryshop.model.Customer;
import com.stationeryshop.model.Product;
import com.stationeryshop.utils.DBConnection;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InvoiceDAO {
    private DBConnection db;
    
    // Constructor mặc định - đọc từ file properties
    public InvoiceDAO(){
        this.db = new DBConnection();
    }
    
    // Constructor với tham số
    public InvoiceDAO(String useradmin, String pwdadmin){
        this.db = new DBConnection(useradmin, pwdadmin);
    }
    
    public boolean saveInvoice(Invoice invoice) {
        Connection conn = null;
        PreparedStatement invoiceStmt = null;
        PreparedStatement detailStmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Insert hóa đơn
            String invoiceSQL = "INSERT INTO invoices (user_id, customer_id, invoice_date, total_amount, discount_amount, status) " +
                                "VALUES (?, ?, ?, ?, ?, ?)";
            invoiceStmt = conn.prepareStatement(invoiceSQL, Statement.RETURN_GENERATED_KEYS);
            invoiceStmt.setString(1, invoice.getUser().getUser_id());
            invoiceStmt.setInt(2, invoice.getCustomer().getId());
            invoiceStmt.setDate(3, Date.valueOf(invoice.getInvoiceDate()));
            invoiceStmt.setDouble(4, invoice.getTotalAmount());
            invoiceStmt.setDouble(5, invoice.getDiscountAmount());
            invoiceStmt.setString(6, invoice.getStatus());

            int affectedRows = invoiceStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            generatedKeys = invoiceStmt.getGeneratedKeys();
            int invoiceId;
            if (generatedKeys.next()) {
                invoiceId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            // 2. Insert chi tiết hóa đơn
            String detailSQL = "INSERT INTO invoice_details (invoice_id, product_id, quantity, unit_price, subtotal) " +
                               "VALUES (?, ?, ?, ?, ?)";
            detailStmt = conn.prepareStatement(detailSQL);

            for (InvoiceDetail detail : invoice.getDetails()) {
                detailStmt.setInt(1, invoiceId);
                detailStmt.setInt(2, detail.getProduct().getProductId());
                detailStmt.setInt(3, detail.getQuantity());
                detailStmt.setDouble(4, detail.getUnitPrice());
                detailStmt.setDouble(5, detail.getSubtotal());
                detailStmt.addBatch();
            }

            detailStmt.executeBatch();
            conn.commit();
            System.out.println("Save invoice success");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // rollback khi lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;

        } finally {
            // Đóng kết nối an toàn
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (detailStmt != null) detailStmt.close();
                if (invoiceStmt != null) invoiceStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Invoice getInvoiceById(int invoiceId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Invoice invoice = null;

        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            
            String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, invoiceId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                invoice = mapInvoiceFromResultSet(rs);
                // Lấy chi tiết hóa đơn
                invoice.setDetails(getInvoiceDetailsInternal(conn, invoiceId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return list;
            }
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM invoices ORDER BY invoice_date DESC");

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetailsInternal(conn, invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
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

    /**
     * Lấy danh sách hóa đơn theo User ID
     */
    public List<Invoice> getInvoicesByUserId(String userId) {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return list;
            }
            
            String sql = "SELECT * FROM invoices WHERE user_id = ? ORDER BY invoice_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetailsInternal(conn, invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
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

    /**
     * Lấy danh sách hóa đơn theo Customer ID
     */
    public List<Invoice> getInvoicesByCustomerId(int customerId) {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return list;
            }
            
            String sql = "SELECT * FROM invoices WHERE customer_id = ? ORDER BY invoice_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetailsInternal(conn, invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
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

    /**
     * Lấy danh sách hóa đơn theo User ID và Status
     */
    public List<Invoice> getInvoicesByUserIdAndStatus(String userId, String status) {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return list;
            }
            
            String sql = "SELECT * FROM invoices WHERE user_id = ? AND status = ? ORDER BY invoice_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setString(2, status);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetailsInternal(conn, invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
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

    public List<Invoice> getInvoicesByDateRange(Date startDate, Date endDate) {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return list;
            }
            
            String sql = "SELECT * FROM invoices WHERE invoice_date BETWEEN ? AND ? ORDER BY invoice_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetailsInternal(conn, invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
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

    /**
     * Lấy danh sách hóa đơn theo User ID và khoảng thời gian
     */
    public List<Invoice> getInvoicesByUserIdAndDateRange(String userId, Date startDate, Date endDate) {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return list;
            }
            
            String sql = "SELECT * FROM invoices WHERE user_id = ? AND invoice_date BETWEEN ? AND ? ORDER BY invoice_date DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetailsInternal(conn, invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
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

    public boolean updateInvoice(int invoiceId, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "UPDATE invoices SET status = ? WHERE invoice_id = ?";
        
        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, invoiceId);
            int rowsAffected = stmt.executeUpdate();
            
            if(rowsAffected > 0) {
                System.out.println("Update invoice success");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean deleteInvoice(int invoiceId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String query = "DELETE FROM invoices WHERE invoice_id = ?";
        
        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, invoiceId);
            int rowsAffected = stmt.executeUpdate();
            
            if(rowsAffected > 0) {
                System.out.println("Delete invoice success");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối an toàn
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Helper method: Lấy chi tiết hóa đơn với connection có sẵn
    private List<InvoiceDetail> getInvoiceDetailsInternal(Connection conn, int invoiceId) throws SQLException {
        List<InvoiceDetail> details = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT * FROM invoice_details WHERE invoice_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, invoiceId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setInvoiceDetailId(rs.getInt("invoice_detail_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitPrice(rs.getDouble("unit_price"));
                detail.setSubtotal(rs.getDouble("subtotal"));

                // Gán Product tạm (nếu cần lấy chi tiết hơn thì join hoặc DAO riêng)
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                detail.setProduct(product);

                details.add(detail);
            }
        } finally {
            // Đóng kết nối an toàn
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return details;
    }

    // Helper method: Lấy chi tiết hóa đơn (public method)
    public List<InvoiceDetail> getInvoiceDetails(int invoiceId) {
        List<InvoiceDetail> details = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = db.connect();
            if(conn == null) {
                JOptionPane.showMessageDialog(null, "The password is incorrect", "Warning", JOptionPane.WARNING_MESSAGE);
                return details;
            }
            
            details = getInvoiceDetailsInternal(conn, invoiceId);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return details;
    }

    private Invoice mapInvoiceFromResultSet(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));

        invoice.setUser(rs.getString("user_id"));
        invoice.setCustomer(rs.getInt("customer_id"));

        invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setDiscountAmount(rs.getDouble("discount_amount"));
        invoice.setFinalAmount(rs.getDouble("final_amount"));
        invoice.setStatus(rs.getString("status"));

        return invoice;
    }
}
