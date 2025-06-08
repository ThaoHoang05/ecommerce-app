package com.stationeryshop.controller;

import com.stationeryshop.dao.InvoiceDAO;
import com.stationeryshop.model.Invoice;
import com.stationeryshop.model.User;
import com.stationeryshop.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    private ScrollPane scrollOrders;

    @FXML
    private Label lblOrderCount;

    @FXML
    private VBox odersListBox;

    @FXML
    private Label totalOrdersCountLbl;

    @FXML
    private Label totalSpentLbl;

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private User currentUser = Session.getCurrentUser();
    private DecimalFormat currencyFormatter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencyFormatter = new DecimalFormat("#,###.##");
        
        // Thiết lập ScrollPane
        scrollOrders.setFitToWidth(true);
        scrollOrders.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollOrders.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    /**
     * Thiết lập user hiện tại và load dữ liệu đơn hàng
     */
    public void setCurrentUser() {
        this.currentUser = Session.getCurrentUser();
        loadOrderHistory();
    }

    /**
     * Load lịch sử đơn hàng của user hiện tại
     */
    private void loadOrderHistory() {
        try {
            // Lấy đơn hàng theo user ID - hiệu quả hơn
            List<Invoice> userInvoices = invoiceDAO.getInvoicesByUserId(currentUser.getUser_id());

            displayOrderHistory(userInvoices);
            updateSummaryLabels(userInvoices);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi cho user
            showErrorMessage("Không thể tải lịch sử đơn hàng: " + e.getMessage());
        }
    }

    /**
     * Hiển thị danh sách đơn hàng
     */
    private void displayOrderHistory(List<Invoice> invoices) {
        odersListBox.getChildren().clear();
        
        if (invoices.isEmpty()) {
            Label noOrdersLabel = new Label("Chưa có đơn hàng nào");
            noOrdersLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666; -fx-padding: 20px;");
            odersListBox.getChildren().add(noOrdersLabel);
            return;
        }

        for (Invoice invoice : invoices) {
            try {
                HBox orderItem = createOrderItem(invoice);
                odersListBox.getChildren().add(orderItem);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Lỗi tạo item cho đơn hàng ID: " + invoice.getInvoiceId());
            }
        }
    }

    /**
     * Tạo một item đơn hàng từ FXML template
     */
    private HBox createOrderItem(Invoice invoice) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/History_Order_Item.fxml"));
        HBox orderItem = loader.load();
        
        // Lấy controller và truyền dữ liệu
        History_Order_ItemController itemController = loader.getController();
        itemController.setInvoiceData(invoice);
        
        return orderItem;
    }

    /**
     * Cập nhật các label tổng kết
     */
    private void updateSummaryLabels(List<Invoice> invoices) {
        int totalOrders = invoices.size();
        double totalSpent = invoices.stream()
                .mapToDouble(Invoice::getFinalAmount)
                .sum();

        totalOrdersCountLbl.setText(String.valueOf(totalOrders));
        lblOrderCount.setText(totalOrders + " đơn hàng");
        totalSpentLbl.setText(currencyFormatter.format(totalSpent) + " VNĐ");
    }

    /**
     * Hiển thị thông báo lỗi
     */
    private void showErrorMessage(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 10px;");
        odersListBox.getChildren().clear();
        odersListBox.getChildren().add(errorLabel);
    }

    /**
     * Refresh lại dữ liệu
     */
    public void refreshData() {
        if (currentUser != null) {
            loadOrderHistory();
        }
    }

    /**
     * Lọc đơn hàng theo trạng thái cho user hiện tại
     */
    public void filterByStatus(String status) {
        try {
            List<Invoice> filteredInvoices;
            
            if (status.equals("ALL")) {
                filteredInvoices = invoiceDAO.getInvoicesByUserId(currentUser.getUser_id());
            } else {
                filteredInvoices = invoiceDAO.getInvoicesByUserIdAndStatus(currentUser.getUser_id(), status);
            }
            
            displayOrderHistory(filteredInvoices);
            updateSummaryLabels(filteredInvoices);
            
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Lỗi khi lọc đơn hàng: " + e.getMessage());
        }
    }

    /**
     * Tìm kiếm đơn hàng theo ID cho user hiện tại
     */
    public void searchByOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            loadOrderHistory(); // Load lại tất cả nếu không có từ khóa
            return;
        }

        try {
            int invoiceId = Integer.parseInt(orderId.trim());
            Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
            
            // Kiểm tra xem hóa đơn có thuộc về user hiện tại không
            if (invoice != null && 
                invoice.getUser() != null && 
                invoice.getUser().getUser_id() != null &&
                invoice.getUser().getUser_id().equals(currentUser.getUser_id())) {
                
                displayOrderHistory(List.of(invoice));
                updateSummaryLabels(List.of(invoice));
            } else {
                showErrorMessage("Không tìm thấy đơn hàng với ID: " + orderId + " trong lịch sử của bạn");
            }
            
        } catch (NumberFormatException e) {
            showErrorMessage("ID đơn hàng không hợp lệ: " + orderId);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }
}
