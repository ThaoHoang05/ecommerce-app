package controller;

import dao.InvoiceDAO;
import dao.InventoryDAO;
import model.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SalesController {

    private final Connection conn;
    private final InvoiceDAO invoiceDAO;
    private final InventoryDAO inventoryDAO;

    public SalesController(Connection conn) {
        this.conn = conn;
        this.invoiceDAO = new InvoiceDAO(); // Nếu cần thì truyền connection tùy thiết kế bạn
        this.inventoryDAO = new InventoryDAO(conn);
    }

    // 1. Tạo hóa đơn mới
    public Invoice createInvoice(User salesPerson, Customer customer) {
        Invoice invoice = new Invoice();
        invoice.setUser(salesPerson);
        invoice.setCustomer(customer);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setStatus("pending");
        invoice.setDetails(new ArrayList<>());
        return invoice;
    }

    // 2. Thêm sản phẩm vào hóa đơn
    public boolean addProductToInvoice(Invoice invoice, Product product, int quantity) throws SQLException {
        int stock = inventoryDAO.getStockLevel(product.getProductId());
        if (stock < quantity) {
            System.out.println("Không đủ hàng trong kho.");
            return false;
        }

        double subtotal = quantity * product.getUnitPrice();

        InvoiceDetail detail = new InvoiceDetail();
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setUnitPrice(product.getUnitPrice());
        detail.setSubtotal(subtotal);
        invoice.getDetails().add(detail);

        calculateTotal(invoice);
        return true;
    }

    // 3. Xóa sản phẩm khỏi hóa đơn
    public void removeProductFromInvoice(Invoice invoice, Product product) {
        invoice.getDetails().removeIf(d -> d.getProduct().getProductId() == product.getProductId());
        calculateTotal(invoice);
    }

    // 4. Tính tổng tiền trước giảm giá
    public void calculateTotal(Invoice invoice) {
        double total = 0;
        for (InvoiceDetail detail : invoice.getDetails()) {
            total += detail.getSubtotal();
        }
        invoice.setTotalAmount(total);
        invoice.setFinalAmount(total - invoice.getDiscountAmount());
    }

    // 5. Áp dụng giảm giá
    public void applyDiscount(Invoice invoice, double discountAmount) {
        invoice.setDiscountAmount(discountAmount);
        invoice.setFinalAmount(invoice.getTotalAmount() - discountAmount);
    }

    // 6. Hoàn tất đơn hàng: lưu hóa đơn và cập nhật tồn kho
    public boolean finalizeSaleAndSaveInvoice(Invoice invoice) throws SQLException {
        try {
            conn.setAutoCommit(false);

            boolean saved = invoiceDAO.saveInvoice(invoice);
            if (!saved) {
                conn.rollback();
                return false;
            }

            for (InvoiceDetail detail : invoice.getDetails()) {
                inventoryDAO.updateStock(
                    detail.getProduct().getProductId(),
                    -detail.getQuantity() // trừ kho
                );
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
