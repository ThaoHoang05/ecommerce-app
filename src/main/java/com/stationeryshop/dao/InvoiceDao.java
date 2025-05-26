package com.stationeryshop.dao;

import com.stationeryshop.model.Invoice;
import model.InvoiceDetail;
import model.User;
import model.Customer;
import model.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public boolean saveInvoice(Invoice invoice) {
        Connection conn = null;
        PreparedStatement invoiceStmt = null;
        PreparedStatement detailStmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Insert hóa đơn
            String invoiceSQL = "INSERT INTO invoices (user_id, customer_id, invoice_date, total_amount, discount_amount, final_amount, status) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
            invoiceStmt = conn.prepareStatement(invoiceSQL, Statement.RETURN_GENERATED_KEYS);
            invoiceStmt.setInt(1, invoice.getUser().getUserId());
            invoiceStmt.setInt(2, invoice.getCustomer().getCustomerId());
            invoiceStmt.setDate(3, Date.valueOf(invoice.getInvoiceDate()));
            invoiceStmt.setDouble(4, invoice.getTotalAmount());
            invoiceStmt.setDouble(5, invoice.getDiscountAmount());
            invoiceStmt.setDouble(6, invoice.getFinalAmount());
            invoiceStmt.setString(7, invoice.getStatus());

            int affectedRows = invoiceStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            ResultSet generatedKeys = invoiceStmt.getGeneratedKeys();
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
            DBConnection.close(conn, invoiceStmt, detailStmt);
        }
    }

    public Invoice getInvoiceById(int invoiceId) {
        Invoice invoice = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, invoiceId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                invoice = mapInvoiceFromResultSet(rs);

                // Lấy chi tiết hóa đơn
                invoice.setDetails(getInvoiceDetails(invoiceId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM invoices");

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetails(invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        return list;
    }

    public List<Invoice> getInvoicesByDateRange(Date startDate, Date endDate) {
        List<Invoice> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM invoices WHERE invoice_date BETWEEN ? AND ?";
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapInvoiceFromResultSet(rs);
                invoice.setDetails(getInvoiceDetails(invoice.getInvoiceId()));
                list.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }

        return list;
    }

    // Helper method: Lấy chi tiết hóa đơn
    private List<InvoiceDetail> getInvoiceDetails(int invoiceId) throws SQLException {
        List<InvoiceDetail> details = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM invoice_details WHERE invoice_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, invoiceId);
        ResultSet rs = stmt.executeQuery();

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

        DBConnection.close(null, stmt, rs);
        return details;
    }

    private Invoice mapInvoiceFromResultSet(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));

        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        invoice.setUser(user);

        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        invoice.setCustomer(customer);

        invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setDiscountAmount(rs.getDouble("discount_amount"));
        invoice.setFinalAmount(rs.getDouble("final_amount"));
        invoice.setStatus(rs.getString("status"));

        return invoice;
    }
}
