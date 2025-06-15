package com.stationeryshop.controller;

import com.stationeryshop.model.Invoice;
import com.stationeryshop.model.InvoiceDetail;
import com.stationeryshop.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class InvoiceDetailController {

    @FXML
    private VBox productsContainerBox;

    @FXML
    private Label invoiceIdLbl;

    @FXML
    private Label totalCostLbl;

    @FXML
    private Label orderDateLbl;

    private List<InvoiceDetail> invoiceDetails;

    private Invoice invoice;

    private Pane parentContainer;

    public void setParentContainer(Pane parentContainer) {
        this.parentContainer = parentContainer;
    }

    @FXML
    void handleGoBack(ActionEvent event) {
        try {
            if (parentContainer != null) {
                // Load ShopView FXML
                final String HISTORY_PATH = "/fxml/History.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(HISTORY_PATH));
                Pane historyPane = loader.load();
                HistoryController historyController = loader.getController();
                historyController.setParentContainer(parentContainer);
                historyController.setCurrentUser();


                // Thay thế nội dung của parent container
                parentContainer.getChildren().clear();
                parentContainer.getChildren().add(historyPane);

                // Nếu parent là AnchorPane, set anchor constraints
                if (parentContainer instanceof javafx.scene.layout.AnchorPane) {
                    javafx.scene.layout.AnchorPane.setTopAnchor(historyPane, 0.0);
                    javafx.scene.layout.AnchorPane.setBottomAnchor(historyPane, 0.0);
                    javafx.scene.layout.AnchorPane.setLeftAnchor(historyPane, 0.0);
                    javafx.scene.layout.AnchorPane.setRightAnchor(historyPane, 0.0);
                }

            } else {
                showErrorAlert("Lỗi", "Không thể quay lại trang sản phẩm - Parent container chưa được thiết lập");
            }

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Lỗi", "Không thể tải giao diện shop view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi", "Có lỗi xảy ra khi chuyển về trang shop: " + e.getMessage());
        }
    }

    public void initialize() {

    }
    void setup(Invoice invoice, List<InvoiceDetail> invoicedetails) {
        this.invoiceDetails = invoicedetails;
        this.invoice = invoice;
        showAllDetails();
        invoiceIdLbl.setText(String.valueOf(invoice.getInvoiceId()));
        orderDateLbl.setText(String.valueOf(invoice.getInvoiceDate()));
        totalCostLbl.setText(String.valueOf(invoice.getFinalAmount()));
    }

    void showAllDetails(){
        for (InvoiceDetail invoiceDetail : invoiceDetails) {
            productsContainerBox.getChildren().add(showInvoiceDetail(invoiceDetail));
        }
    }
    HBox showInvoiceDetail(InvoiceDetail invoiceDetail) {
        Product product = invoiceDetail.getProduct();
        Label nameLabel = new Label(product.getProductName());
        nameLabel.setStyle("-fx-text-fill: #495057; -fx-font-size: 15px; -fx-font-weight: bold;");
        nameLabel.setPrefWidth(200);

        Label priceLabel = new Label(String.valueOf(invoiceDetail.getUnitPrice()));
        priceLabel.setStyle("-fx-text-fill: #495057; -fx-font-size: 15px; -fx-font-weight: bold;");
        priceLabel.setPrefWidth(80);

        Label quantityLabel = new Label(String.valueOf(invoiceDetail.getQuantity()));
        quantityLabel.setStyle("-fx-text-fill: #495057; -fx-font-size: 15px; -fx-font-weight: bold;");
        quantityLabel.setPrefWidth(80);

        Label costLabel = new Label(String.valueOf(invoiceDetail.getSubtotal()));
        costLabel.setStyle("-fx-text-fill: #495057; -fx-font-size: 15px; -fx-font-weight: bold;");
        costLabel.setPrefWidth(100);

        // Tạo HBox và thêm các nhãn vào
        HBox hbox = new HBox(20, nameLabel, priceLabel, quantityLabel, costLabel);
        hbox.setStyle("-fx-background-color: #E9ECEF; -fx-padding: 0 20;");
        return hbox;
    }
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Hiển thị thông báo thành công
     */
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

