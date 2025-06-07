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
 * Provides methods to retrieve data for different report types including
 * daily/weekly/monthly revenue reports and top-selling products analysis.
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
     * Gets daily revenue for a specific date.
     * 
     * @param date The date to get revenue for
     * @return BigDecimal representing total revenue for the day
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getDailyRevenue(LocalDate date) throws SQLException {
        BigDecimal revenue = BigDecimal.ZERO;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT COALESCE(SUM(total_amount), 0) AS daily_revenue " +
                     "FROM invoices " +
                     "WHERE DATE(invoice_date) = ? " +
                     "AND payment_status = 'PAID'";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(date));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                revenue = rs.getBigDecimal("daily_revenue");
                if (revenue == null) {
                    revenue = BigDecimal.ZERO;
                }
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return revenue;
    }
    
    /**
     * Gets weekly revenue between two dates.
     * 
     * @param startDate Start date of the week
     * @param endDate End date of the week
     * @return BigDecimal representing total revenue for the week
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getWeeklyRevenue(LocalDate startDate, LocalDate endDate) throws SQLException {
        BigDecimal revenue = BigDecimal.ZERO;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT COALESCE(SUM(total_amount), 0) AS weekly_revenue " +
                     "FROM invoices " +
                     "WHERE DATE(invoice_date) BETWEEN ? AND ? " +
                     "AND payment_status = 'PAID'";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                revenue = rs.getBigDecimal("weekly_revenue");
                if (revenue == null) {
                    revenue = BigDecimal.ZERO;
                }
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return revenue;
    }
    
    /**
     * Gets monthly revenue for a specific month and year.
     * 
     * @param month Month (1-12)
     * @param year Year
     * @return BigDecimal representing total revenue for the month
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getMonthlyRevenue(int month, int year) throws SQLException {
        BigDecimal revenue = BigDecimal.ZERO;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT COALESCE(SUM(total_amount), 0) AS monthly_revenue " +
                     "FROM invoices " +
                     "WHERE EXTRACT(MONTH FROM invoice_date) = ? " +
                     "AND EXTRACT(YEAR FROM invoice_date) = ? " +
                     "AND payment_status = 'PAID'";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                revenue = rs.getBigDecimal("monthly_revenue");
                if (revenue == null) {
                    revenue = BigDecimal.ZERO;
                }
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return revenue;
    }
    
    /**
     * Gets revenue for a specific date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return BigDecimal representing total revenue for the date range
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        BigDecimal revenue = BigDecimal.ZERO;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT COALESCE(SUM(total_amount), 0) AS range_revenue " +
                     "FROM invoices " +
                     "WHERE DATE(invoice_date) BETWEEN ? AND ? " +
                     "AND payment_status = 'PAID'";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                revenue = rs.getBigDecimal("range_revenue");
                if (revenue == null) {
                    revenue = BigDecimal.ZERO;
                }
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return revenue;
    }
    
    /**
     * Gets top selling products by quantity in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Maximum number of products to return
     * @return List of maps containing product sales data
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getTopSellingProducts(LocalDate startDate, LocalDate endDate, int limit) throws SQLException {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                     "    p.product_id, " +
                     "    p.product_name, " +// product_code ???
                     "    SUM(id.quantity) AS total_quantity, " +
                     "    SUM(id.line_total) AS total_revenue, " +
                     "    AVG(id.unit_price) AS avg_price " +
                     "FROM invoice_details id " +
                     "JOIN products p ON id.product_id = p.product_id " +
                     "JOIN invoices i ON id.invoice_id = i.invoice_id " +
                     "WHERE DATE(i.invoice_date) BETWEEN ? AND ? " +
                     "AND i.payment_status = 'PAID' " +
                     "GROUP BY p.product_id, p.product_name " +
                     "ORDER BY total_quantity DESC " +
                     "LIMIT ?";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setInt(3, limit);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("productId", rs.getInt("product_id"));
                product.put("productName", rs.getString("product_name"));
                product.put("totalQuantity", rs.getInt("total_quantity"));
                product.put("totalRevenue", rs.getBigDecimal("total_revenue"));
                product.put("averagePrice", rs.getBigDecimal("avg_price"));
                
                topProducts.add(product);
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return topProducts;
    }
    
    /**
     * Gets top selling products by revenue in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @param limit Maximum number of products to return
     * @return List of maps containing product sales data ordered by revenue
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getTopSellingProductsByRevenue(LocalDate startDate, LocalDate endDate, int limit) throws SQLException {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                     "    p.product_id, " +
                     "    p.product_name, " +
                     "    SUM(id.quantity) AS total_quantity, " +
                     "    SUM(id.line_total) AS total_revenue, " +
                     "    AVG(id.unit_price) AS avg_price " +
                     "FROM invoice_details id " +
                     "JOIN products p ON id.product_id = p.product_id " +
                     "JOIN invoices i ON id.invoice_id = i.invoice_id " +
                     "WHERE DATE(i.invoice_date) BETWEEN ? AND ? " +
                     "AND i.payment_status = 'PAID' " +
                     "GROUP BY p.product_id, p.product_name" +
                     "ORDER BY total_revenue DESC " +
                     "LIMIT ?";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            stmt.setInt(3, limit);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("productId", rs.getInt("product_id"));
                product.put("productName", rs.getString("product_name"));
                product.put("totalQuantity", rs.getInt("total_quantity"));
                product.put("totalRevenue", rs.getBigDecimal("total_revenue"));
                product.put("averagePrice", rs.getBigDecimal("avg_price"));
                
                topProducts.add(product);
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return topProducts;
    }
    
    /**
     * Gets sales report by staff member in a date range.
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of maps containing staff sales data
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getStaffSalesReport(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Map<String, Object>> staffSales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                     "    u.user_id, " +
                     "    u.full_name, " +
                     "    u.username, " +
                     "    COUNT(i.invoice_id) AS total_invoices, " +
                     "    SUM(i.total_amount) AS total_revenue, " +
                     "    AVG(i.total_amount) AS avg_invoice " +
                     "FROM users u " +
                     "LEFT JOIN invoices i ON u.user_id = i.created_by " +
                     "    AND DATE(i.invoice_date) BETWEEN ? AND ? " +
                     "    AND i.payment_status = 'PAID' " +
                     "WHERE u.role IN ('STAFF', 'MANAGER') " +
                     "GROUP BY u.user_id, u.full_name, u.username " +
                     "ORDER BY total_revenue DESC NULLS LAST";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> staff = new HashMap<>();
                staff.put("userId", rs.getInt("user_id"));
                staff.put("fullName", rs.getString("full_name"));
                staff.put("username", rs.getString("username"));
                staff.put("totalInvoices", rs.getInt("total_invoices"));
                staff.put("totalRevenue", rs.getBigDecimal("total_revenue"));
                staff.put("averageInvoice", rs.getBigDecimal("avg_invoice"));
                
                staffSales.add(staff);
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return staffSales;
    }
    
    /**
     * Gets inventory report showing current stock levels.
     * 
     * @return List of maps containing inventory data
     * @throws SQLException if a database error occurs
     */
    public List<Map<String, Object>> getInventoryReport() throws SQLException {
        List<Map<String, Object>> inventory = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT " +
                     "    p.product_id, " +
                     "    p.product_name, " +
                     "    p.product_code, " +
                     "    p.stock_quantity, " +
                     "    p.unit_price, " +
                     "    p.stock_quantity * p.unit_price AS stock_value, " +
                     "    c.category_name, " +
                     "    CASE " +
                     "        WHEN p.stock_quantity <= 10 THEN 'LOW' " +
                     "        WHEN p.stock_quantity <= 50 THEN 'MEDIUM' " +
                     "        ELSE 'HIGH' " +
                     "    END AS stock_level " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.is_active = true " +
                     "ORDER BY p.stock_quantity ASC, p.product_name";
        
        try {
            conn = db.connect();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("productId", rs.getInt("product_id"));
                item.put("productName", rs.getString("product_name"));
                item.put("productCode", rs.getString("product_code"));
                item.put("stockQuantity", rs.getInt("stock_quantity"));
                item.put("unitPrice", rs.getBigDecimal("unit_price"));
                item.put("stockValue", rs.getBigDecimal("stock_value"));
                item.put("categoryName", rs.getString("category_name"));
                item.put("stockLevel", rs.getString("stock_level"));
                
                inventory.add(item);
            }
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return inventory;
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
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return invoices;
    }
    
    /**
     * Gets total revenue for a given time period with summary statistics.
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
                "WHERE DATE(invoice_date) BETWEEN ? AND ? " +
                "AND payment_status = 'PAID'";
        
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
        } finally {
            closeResources(rs, stmt, conn);
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
                "AND payment_status = 'PAID' " +
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
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return dailyRevenue;
    }
    
    /**
     * Helper method to close database resources safely.
     * 
     * @param rs ResultSet to close
     * @param stmt Statement to close
     * @param conn Connection to close
     */
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
    }

    /**
     * Closes the database connection when done with this DAO.
     */
}
