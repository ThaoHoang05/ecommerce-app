package com.stationeryshop.dao;

import com.stationeryshop.model.Invoice;
import com.stationeryshop.utils.DBConnection;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Data Access Object for generating reports related to sales and invoices.
 * Provides methods to retrieve data for different report types.
 */
public class ReportDAO {
    private DBConnection db;
    
    /**
     * Constructs a new ReportDAO instance and initializes the database connection.
     */
    public ReportDAO() {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/db.properties");
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String useradmin = props.getProperty("db.admin");
        String pwdadmin = props.getProperty("db.adminpwd");
        this.db = new DBConnection(useradmin, pwdadmin);
    }
    
    public ReportDAO(String useradmin, String pwdadmin) {
        this.db = new DBConnection(useradmin, pwdadmin);
    }
    
    /**
     * Retrieves a list of invoices for a specific date.
     * 
     * @param date The date to get invoices for
     * @return List of invoice data as maps containing all required fields
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getInvoicesByDate(LocalDate date) throws SQLException {
        List<Map<String, Object>> invoices = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "i.invoice_id, " +
                "i.invoice_date, " +
                "i.total_amount, " +
                "i.payment_status, " +
                "c.customer_id, " +
                "c.customer_name, " +
                "c.phone_number, " +
                "u.user_id, " +
                "u.full_name AS staff_name " +
                "FROM invoices i " +
                "LEFT JOIN customers c ON i.customer_id = c.customer_id " +
                "LEFT JOIN users u ON i.created_by = u.user_id " +
                "WHERE DATE(i.invoice_date) = ? " +
                "ORDER BY i.invoice_date DESC";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("invoiceId", rs.getInt("invoice_id"));
                invoice.put("invoiceDate", rs.getTimestamp("invoice_date"));
                invoice.put("totalAmount", rs.getBigDecimal("total_amount"));
                invoice.put("paymentStatus", rs.getString("payment_status"));
                invoice.put("customerId", rs.getInt("customer_id"));
                invoice.put("customerName", rs.getString("customer_name"));
                invoice.put("phoneNumber", rs.getString("phone_number"));
                invoice.put("userId", rs.getInt("user_id"));
                invoice.put("staffName", rs.getString("staff_name"));
                
                invoices.add(invoice);
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
        
        return invoices;
    }
    
    /**
     * Gets invoice summary information for a specific date.
     * 
     * @param date The date to get summary information for
     * @return Map containing total invoice count and total revenue
     * @throws SQLException if a database error occurs
     */
    public Map<String, Object> getInvoiceSummaryByDate(LocalDate date) throws SQLException {
        Map<String, Object> summary = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "COUNT(invoice_id) AS total_invoices, " +
                "SUM(total_amount) AS total_revenue " +
                "FROM invoices " +
                "WHERE DATE(invoice_date) = ?";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                summary.put("totalInvoices", rs.getInt("total_invoices"));
                summary.put("totalRevenue", rs.getBigDecimal("total_revenue"));
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
        
        return summary;
    }
    
    /**
     * Retrieves the details of a specific invoice.
     * 
     * @param invoiceId The ID of the invoice to retrieve details for
     * @return List of invoice line items as maps
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getInvoiceDetails(int invoiceId) throws SQLException {
        List<Map<String, Object>> details = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "id.invoice_detail_id, " +
                "id.invoice_id, " +
                "id.product_id, " +
                "p.product_name, " +
                "p.product_code, " +
                "id.quantity, " +
                "id.unit_price, " +
                "id.discount, " +
                "id.line_total " +
                "FROM invoice_details id " +
                "JOIN products p ON id.product_id = p.product_id " +
                "WHERE id.invoice_id = ? " +
                "ORDER BY id.invoice_detail_id";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, invoiceId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("invoiceDetailId", rs.getInt("invoice_detail_id"));
                detail.put("invoiceId", rs.getInt("invoice_id"));
                detail.put("productId", rs.getInt("product_id"));
                detail.put("productName", rs.getString("product_name"));
                detail.put("productCode", rs.getString("product_code"));
                detail.put("quantity", rs.getInt("quantity"));
                detail.put("unitPrice", rs.getBigDecimal("unit_price"));
                detail.put("discount", rs.getBigDecimal("discount"));
                detail.put("lineTotal", rs.getBigDecimal("line_total"));
                
                details.add(detail);
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
        
        return details;
    }
    
    /**
     * Retrieves invoices for a specific date filtered by payment status.
     * 
     * @param date The date to get invoices for
     * @param paymentStatus The payment status to filter by
     * @return List of invoices matching the criteria
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getInvoicesByDateAndPaymentStatus(LocalDate date, String paymentStatus) throws SQLException {
        List<Map<String, Object>> invoices = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "i.invoice_id, " +
                "i.invoice_date, " +
                "i.total_amount, " +
                "i.payment_status, " +
                "c.customer_name, " +
                "u.full_name AS staff_name " +
                "FROM invoices i " +
                "LEFT JOIN customers c ON i.customer_id = c.customer_id " +
                "LEFT JOIN users u ON i.created_by = u.user_id " +
                "WHERE DATE(i.invoice_date) = ? " +
                "AND i.payment_status = ? " +
                "ORDER BY i.invoice_date DESC";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, paymentStatus);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("invoiceId", rs.getInt("invoice_id"));
                invoice.put("invoiceDate", rs.getTimestamp("invoice_date"));
                invoice.put("totalAmount", rs.getBigDecimal("total_amount"));
                invoice.put("paymentStatus", rs.getString("payment_status"));
                invoice.put("customerName", rs.getString("customer_name"));
                invoice.put("staffName", rs.getString("staff_name"));
                
                invoices.add(invoice);
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
        
        return invoices;
    }
    
    /**
     * Retrieves invoices for a specific date created by a specific staff member.
     * 
     * @param date The date to get invoices for
     * @param userId The ID of the staff member
     * @return List of invoices matching the criteria
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getInvoicesByDateAndStaff(LocalDate date, int userId) throws SQLException {
        List<Map<String, Object>> invoices = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "i.invoice_id, " +
                "i.invoice_date, " +
                "i.total_amount, " +
                "i.payment_status, " +
                "c.customer_name, " +
                "u.full_name AS staff_name " +
                "FROM invoices i " +
                "LEFT JOIN customers c ON i.customer_id = c.customer_id " +
                "LEFT JOIN users u ON i.created_by = u.user_id " +
                "WHERE DATE(i.invoice_date) = ? " +
                "AND i.created_by = ? " +
                "ORDER BY i.invoice_date DESC";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            stmt.setInt(2, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("invoiceId", rs.getInt("invoice_id"));
                invoice.put("invoiceDate", rs.getTimestamp("invoice_date"));
                invoice.put("totalAmount", rs.getBigDecimal("total_amount"));
                invoice.put("paymentStatus", rs.getString("payment_status"));
                invoice.put("customerName", rs.getString("customer_name"));
                invoice.put("staffName", rs.getString("staff_name"));
                
                invoices.add(invoice);
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
        
        return invoices;
    }
    
    /**
     * Gets the number of unique customers who made purchases on a specific date.
     * 
     * @param date The date to check
     * @return Number of unique customers
     * @throws SQLException if a database error occurs
     */
    public int getUniqueCustomerCountByDate(LocalDate date) throws SQLException {
        int uniqueCustomers = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "COUNT(DISTINCT customer_id) AS unique_customers " +
                "FROM invoices " +
                "WHERE DATE(invoice_date) = ? " +
                "AND customer_id IS NOT NULL";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                uniqueCustomers = rs.getInt("unique_customers");
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
        
        return uniqueCustomers;
    }
    
    /**
     * Gets total revenue for a given time period (daily, weekly, monthly).
     * 
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     * @return Map containing revenue summary information
     * @throws SQLException if a database error occurs
     */
    public Map<String, Object> getRevenueSummary(LocalDate startDate, LocalDate endDate) throws SQLException {
        Map<String, Object> summary = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "COUNT(invoice_id) AS total_invoices, " +
                "SUM(total_amount) AS total_revenue, " +
                "AVG(total_amount) AS average_invoice, " +
                "COUNT(DISTINCT customer_id) AS unique_customers " +
                "FROM invoices " +
                "WHERE DATE(invoice_date) BETWEEN ? AND ?";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                summary.put("totalInvoices", rs.getInt("total_invoices"));
                summary.put("totalRevenue", rs.getBigDecimal("total_revenue"));
                summary.put("averageInvoice", rs.getBigDecimal("average_invoice"));
                summary.put("uniqueCustomers", rs.getInt("unique_customers"));
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
        
        return summary;
    }
    
    /**
     * Gets revenue data by day within a date range, useful for chart display.
     * 
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     * @return List of daily revenue data points
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getDailyRevenueData(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Map<String, Object>> dailyRevenue = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                "DATE(invoice_date) AS day, " +
                "COUNT(invoice_id) AS invoice_count, " +
                "SUM(total_amount) AS daily_revenue " +
                "FROM invoices " +
                "WHERE DATE(invoice_date) BETWEEN ? AND ? " +
                "GROUP BY DATE(invoice_date) " +
                "ORDER BY DATE(invoice_date)";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", rs.getDate("day").toLocalDate());
                dayData.put("invoiceCount", rs.getInt("invoice_count"));
                dayData.put("revenue", rs.getBigDecimal("daily_revenue"));
                
                dailyRevenue.add(dayData);
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
        
        return dailyRevenue;
    }
    
    /**
     * Closes the database connection when done with this DAO.
     */
    public void close() {
        db.closeConnect();
    }
}
