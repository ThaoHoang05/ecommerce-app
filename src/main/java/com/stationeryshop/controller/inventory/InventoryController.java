package com.stationeryshop.controller.inventory;

import com.stationeryshop.dao.InventoryProductDAO;
import com.stationeryshop.model.InventoryProduct;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    @FXML
    private TableView<InventoryProduct> inventoryTable;

    @FXML
    private Button refreshBtn;

    @FXML
    private TableColumn<InventoryProduct, String> invLastStockedColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<InventoryProduct, Integer> invQuantityColumn;

    @FXML
    private Button searchBtn;

    @FXML
    private TableColumn<InventoryProduct, String> invProductColumn;

    @FXML
    private TableColumn<InventoryProduct, Integer> invIdColumn;

    private InventoryProductDAO inventoryProductDAO;
    private static ObservableList<InventoryProduct> inventoryData;
    private FilteredList<InventoryProduct> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo DAO
        inventoryProductDAO = new InventoryProductDAO();
        inventoryData = inventoryProductDAO.getAllInventoryProduct();
        // Thiết lập các cột của TableView
        setupTableColumns();

        // Load dữ liệu ban đầu
        loadInventoryData();

        // Thiết lập tìm kiếm theo thời gian thực
        setupSearchFilter();

        // Thiết lập context menu cho TableView
        setupTableContextMenu();
    }

    /**
     * Thiết lập các cột của TableView
     */
    private void setupTableColumns() {
        // Cột ID
        invIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));

        // Cột Sản phẩm
        invProductColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        // Cột Tồn kho
        invQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Cột Ngày nhập - cần custom cell factory vì InventoryProduct không có trực tiếp lastStockedDate
        invLastStockedColumn.setCellValueFactory(cellData -> {
            // Giả sử bạn sẽ thêm phương thức getLastStockedDate() vào InventoryProduct
            // Hiện tại return một giá trị mặc định
            if(cellData.getValue() != null) {
                return new SimpleStringProperty(String.valueOf(cellData.getValue().getLastStockedDate()));
            }else {
                return new javafx.beans.property.SimpleStringProperty("N/A");
            }
            });

        // Căn giữa cho cột ID và Quantity
        invIdColumn.setStyle("-fx-alignment: CENTER;");
        invQuantityColumn.setStyle("-fx-alignment: CENTER;");
        invLastStockedColumn.setStyle("-fx-alignment: CENTER;");

        // Thiết lập cell factory cho cột quantity để highlight số lượng thấp
        invQuantityColumn.setCellFactory(column -> {
            return new TableCell<InventoryProduct, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.toString());

                        // Highlight màu đỏ nếu số lượng < 10
                        if (item < 10) {
                            setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828;");
                        } else if (item < 50) {
                            setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;");
                        } else {
                            setStyle("-fx-text-fill: #2e7d32;");
                        }
                    }
                }
            };
        });
    }

    /**
     * Load dữ liệu tồn kho từ database
     */
    private void loadInventoryData() {
        try {
            // Tạo FilteredList để hỗ trợ tìm kiếm
            filteredData = new FilteredList<>(inventoryData, p -> true);

            // Set dữ liệu cho TableView
            inventoryTable.setItems(filteredData);

            // Hiển thị thông tin tổng quan
            updateStatusInfo();

        } catch (Exception e) {
            showErrorAlert("Lỗi tải dữ liệu", "Không thể tải dữ liệu tồn kho: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Thiết lập tìm kiếm theo thời gian thực
     */
    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(inventoryProduct -> {
                // Nếu không có từ khóa tìm kiếm, hiển thị all
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Tìm kiếm theo tên sản phẩm
                if (inventoryProduct.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Tìm kiếm theo mô tả
                if (inventoryProduct.getProductDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Tìm kiếm theo category
                if (inventoryProduct.getCategoryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Tìm kiếm theo ID sản phẩm
                if (String.valueOf(inventoryProduct.getProductId()).contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Không tìm thấy
            });
        });
    }

    /**
     * Thiết lập context menu cho TableView
     */
    private void setupTableContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem viewDetailsItem = new MenuItem("Xem chi tiết");
        viewDetailsItem.setOnAction(e -> viewProductDetails());

        MenuItem refreshItem = new MenuItem("Làm mới");
        refreshItem.setOnAction(e -> handleRefresh(null));

        contextMenu.getItems().addAll(viewDetailsItem, new SeparatorMenuItem(), refreshItem);

        inventoryTable.setContextMenu(contextMenu);
    }

    /**
     * Xử lý sự kiện tìm kiếm
     */
    @FXML
    void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            showInfoAlert("Thông báo", "Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        // Tìm kiếm đã được xử lý trong setupSearchFilter()
        // Chỉ cần focus vào bảng kết quả
        if (filteredData.isEmpty()) {
            showInfoAlert("Không tìm thấy", "Không có sản phẩm nào phù hợp với từ khóa: " + searchText);
        } else {
            // Focus vào item đầu tiên trong kết quả
            inventoryTable.getSelectionModel().selectFirst();
            inventoryTable.scrollTo(0);
        }
    }

    /**
     * Xử lý sự kiện làm mới dữ liệu
     */
    @FXML
    void handleRefresh(ActionEvent event) {
        // Clear search field
        searchField.clear();

        // Reload dữ liệu
        loadInventoryData();

        showInfoAlert("Thành công", "Đã làm mới dữ liệu tồn kho!");
    }

    /**
     * Xem chi tiết sản phẩm được chọn
     */
    private void viewProductDetails() {
        InventoryProduct selectedProduct = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showWarningAlert("Chưa chọn sản phẩm", "Vui lòng chọn một sản phẩm để xem chi tiết!");
            return;
        }

        // Tạo dialog hiển thị chi tiết
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết sản phẩm");
        alert.setHeaderText("Thông tin chi tiết sản phẩm");

        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(selectedProduct.getProductId()).append("\n");
        details.append("Tên sản phẩm: ").append(selectedProduct.getProductName()).append("\n");
        details.append("Mô tả: ").append(selectedProduct.getProductDescription()).append("\n");
        details.append("Giá: ").append(String.format("%.2f VND", selectedProduct.getProductPrice())).append("\n");
        details.append("Số lượng tồn: ").append(selectedProduct.getQuantity()).append("\n");
        details.append("Danh mục: ").append(selectedProduct.getCategoryName()).append("\n");
        details.append("Nhà cung cấp: ").append(String.join(", ", selectedProduct.getSupplierName()));

        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    /**
     * Cập nhật thông tin trạng thái
     */
    private void updateStatusInfo() {
        if (inventoryData == null) return;

        int totalProducts = inventoryData.size();
        int lowStockProducts = (int) inventoryData.stream()
                .mapToInt(InventoryProduct::getQuantity)
                .filter(qty -> qty < 10)
                .count();

        // Bạn có thể thêm Label để hiển thị thông tin này trong FXML
        System.out.println("Tổng số sản phẩm: " + totalProducts);
        System.out.println("Sản phẩm sắp hết hàng: " + lowStockProducts);
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
     * Hiển thị thông báo cảnh báo
     */
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Lấy sản phẩm được chọn
     */
    public InventoryProduct getSelectedProduct() {
        return inventoryTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Refresh dữ liệu từ bên ngoài
     */
    public void refreshData() {
        loadInventoryData();
    }
}