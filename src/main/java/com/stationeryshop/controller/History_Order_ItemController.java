
package com.stationeryshop.controller;

import com.stationeryshop.dao.InvoiceDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.Invoice;
import com.stationeryshop.model.InvoiceDetail;
import com.stationeryshop.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class History_Order_ItemController implements Initializable {

    @FXML
    private Label orderID_Lbl;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Button viewDetailsBtn;

    @FXML
    private Label orderDateLbl;

    @FXML
    private Label statusLbl;

    private Invoice currentInvoice;
    private DecimalFormat currencyFormatter;
    private DateTimeFormatter dateFormatter;
    private InvoiceDAO invoiceDAO;
    private ProductDAO productDAO;

    private Pane parent;

    public void setParentContainer(Pane parentContainer) {
        this.parent = parentContainer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencyFormatter = new DecimalFormat("#,###.##");
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        invoiceDAO = new InvoiceDAO();
        productDAO = new ProductDAO();
        
        // Thiết lập style cho button
        setupButtonStyle();
    }

    /**
     * Thiết lập dữ liệu cho invoice item
     */
    public void setInvoiceData(Invoice invoice) {
        this.currentInvoice = invoice;
        displayInvoiceInfo();
    }

    /**
     * Hiển thị thông tin invoice
     */
    private void displayInvoiceInfo() {
        if (currentInvoice == null) return;

        try {
            // Hiển thị ID đơn hàng
            orderID_Lbl.setText("#" + currentInvoice.getInvoiceId());
            
            // Hiển thị ngày tạo đơn
            if (currentInvoice.getInvoiceDate() != null) {
                orderDateLbl.setText(currentInvoice.getInvoiceDate().format(dateFormatter));
            } else {
                orderDateLbl.setText("N/A");
            }
            
            // Hiển thị tổng tiền
            lblTotalAmount.setText(currencyFormatter.format(currentInvoice.getFinalAmount()) + " VNĐ");
            
            // Hiển thị trạng thái (nếu có label status)
            if (statusLbl != null) {
                String status = currentInvoice.getStatus();
                statusLbl.setText(getStatusText(status));
                statusLbl.setStyle(getStatusStyle(status));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi hiển thị thông tin đơn hàng: " + e.getMessage());
        }
    }

    /**
     * Chuyển đổi status code thành text hiển thị
     */
    private String getStatusText(String status) {
        if (status == null) return "Không xác định";
        
        return switch (status.toLowerCase()) {
            case "pending" -> "Đang xử lý";
            case "completed" -> "Hoàn thành";
            case "cancelled" -> "Đã hủy";
            case "processing" -> "Đang chuẩn bị";
            case "shipped" -> "Đã giao";
            default -> status;
        };
    }

    /**
     * Lấy style CSS cho trạng thái
     */
    private String getStatusStyle(String status) {
        if (status == null) return "-fx-text-fill: #666666;";
        
        return switch (status.toLowerCase()) {
            case "pending" -> "-fx-text-fill: #ff9800; -fx-font-weight: bold;";
            case "completed" -> "-fx-text-fill: #4caf50; -fx-font-weight: bold;";
            case "cancelled" -> "-fx-text-fill: #f44336; -fx-font-weight: bold;";
            case "processing" -> "-fx-text-fill: #2196f3; -fx-font-weight: bold;";
            case "shipped" -> "-fx-text-fill: #9c27b0; -fx-font-weight: bold;";
            default -> "-fx-text-fill: #666666;";
        };
    }

    /**
     * Thiết lập style cho button
     */
    private void setupButtonStyle() {
        if (viewDetailsBtn != null) {
            viewDetailsBtn.setStyle(
                "-fx-background-color: #2196f3; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: hand;"
            );
            
            // Hover effect
            viewDetailsBtn.setOnMouseEntered(_ -> 
                viewDetailsBtn.setStyle(
                    "-fx-background-color: #1976d2; " +
                    "-fx-text-fill: white; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;"
                )
            );
            
            viewDetailsBtn.setOnMouseExited(_ -> 
                viewDetailsBtn.setStyle(
                    "-fx-background-color: #2196f3; " +
                    "-fx-text-fill: white; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-cursor: hand;"
                )
            );
        }
    }

    /**
     * Xử lý khi click vào ID đơn hàng (có thể copy to clipboard)
     */
    @FXML
    void ID(ActionEvent event) {
        try {
            if (currentInvoice != null) {
                // Copy ID to clipboard
                javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                content.putString(String.valueOf(currentInvoice.getInvoiceId()));
                clipboard.setContent(content);
                
                // Hiển thị thông báo
                showInfoAlert("Thông báo", "Đã copy ID đơn hàng: " + currentInvoice.getInvoiceId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi", "Không thể copy ID đơn hàng: " + e.getMessage());
        }
    }

    /**
     * Xử lý khi click vào nút "Xem chi tiết"
     */
    @FXML
    void handleViewDetails(javafx.event.ActionEvent event) {
        if (currentInvoice == null) {
            showErrorAlert("Lỗi", "Không có thông tin đơn hàng để hiển thị");
            return;
        }

        try {
            // Load chi tiết đầy đủ từ database
            List<InvoiceDetail> fullInvoice = invoiceDAO.getInvoiceDetailsInternal(currentInvoice.getInvoiceId());
            if (fullInvoice == null) {
                showErrorAlert("Lỗi", "Không tìm thấy thông tin chi tiết đơn hàng");
            }
            //Load vao main pane
            final String INVOICE_DETAILS_PATH = "/fxml/InvoiceDetail.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(INVOICE_DETAILS_PATH));
            Pane detailPane = loader.load();
            InvoiceDetailController controller = loader.getController();
            controller.setParentContainer(parent);
            controller.setup(currentInvoice,fullInvoice);
            parent.getChildren().clear();
            parent.getChildren().add(detailPane);
            if (parent instanceof javafx.scene.layout.AnchorPane) {
                javafx.scene.layout.AnchorPane.setTopAnchor(detailPane, 0.0);
                javafx.scene.layout.AnchorPane.setBottomAnchor(detailPane, 0.0);
                javafx.scene.layout.AnchorPane.setLeftAnchor(detailPane, 0.0);
                javafx.scene.layout.AnchorPane.setRightAnchor(detailPane, 0.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi", "Không thể hiển thị chi tiết đơn hàng: " + e.getMessage());
        }
    }

    /**
     * Load thông tin chi tiết sản phẩm cho invoice
     */
    private void loadProductDetailsForInvoice(Invoice invoice) {
        if (invoice.getDetails() != null) {
            for (InvoiceDetail detail : invoice.getDetails()) {
                if (detail.getProduct() != null && detail.getProduct().getProductId() > 0) {
                    Product fullProduct = productDAO.getProductById(detail.getProduct().getProductId());
                    if (fullProduct != null) {
                        detail.setProduct(fullProduct);
                    }
                }
            }
        }
    }

    /**
     * Mở popup hiển thị chi tiết đơn hàng
     */
    private void openOrderDetailsPopup(Invoice invoice) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OrderDetailsPopup.fxml"));
            Parent root = loader.load();
            
            // Nếu có controller cho popup, truyền dữ liệu vào
            // OrderDetailsController controller = loader.getController();
            // controller.setInvoiceData(invoice);
            
            Stage popup = new Stage();
            popup.setTitle("Chi tiết đơn hàng #" + invoice.getInvoiceId());
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setScene(new Scene(root));
            popup.setResizable(false);
            popup.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback: hiển thị thông tin bằng Alert dialog
            showOrderDetailsInAlert(invoice);
        }
    }

    /**
     * Hiển thị chi tiết đơn hàng bằng Alert (fallback)
     */
    private void showOrderDetailsInAlert(Invoice invoice) {
        StringBuilder details = new StringBuilder();
        details.append("Đơn hàng #").append(invoice.getInvoiceId()).append("\n");
        details.append("Ngày: ").append(invoice.getInvoiceDate().format(dateFormatter)).append("\n");
        details.append("Trạng thái: ").append(getStatusText(invoice.getStatus())).append("\n\n");
        
        if (invoice.getCustomer() != null) {
            details.append("Khách hàng ID: ").append(invoice.getCustomer().getId()).append("\n");
        }
        
        details.append("Chi tiết sản phẩm:\n");
        if (invoice.getDetails() != null && !invoice.getDetails().isEmpty()) {
            for (InvoiceDetail detail : invoice.getDetails()) {
                Product product = detail.getProduct();
                String productName = (product != null && product.getProductName() != null) 
                    ? product.getProductName() 
                    : "Sản phẩm ID: " + (product != null ? product.getProductId() : "N/A");
                
                details.append("- ").append(productName)
                       .append(" x").append(detail.getQuantity())
                       .append(" = ").append(currencyFormatter.format(detail.getSubtotal())).append(" VNĐ\n");
            }
        } else {
            details.append("Không có chi tiết sản phẩm\n");
        }
        
        details.append("\nTổng tiền: ").append(currencyFormatter.format(invoice.getTotalAmount())).append(" VNĐ");
        details.append("\nGiảm giá: ").append(currencyFormatter.format(invoice.getDiscountAmount())).append(" VNĐ");
        details.append("\nThành tiền: ").append(currencyFormatter.format(invoice.getFinalAmount())).append(" VNĐ");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết đơn hàng");
        alert.setHeaderText("Thông tin chi tiết đơn hàng");
        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo lỗi
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo thông tin
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Getter cho invoice hiện tại
     */
    public Invoice getCurrentInvoice() {
        return currentInvoice;
    }
}