package com.stationeryshop.controller.inventory;

// Thêm vào class InventoryProductController
// T hỏi claude thì nó chỉ thế nay

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.application.Platform;
import java.util.stream.Collectors;

public class Controller_ThamKhao_Cho_ComboBox {

    @FXML
    private ComboBox<String> categoryComboBox;

    // Danh sách tất cả danh mục (dữ liệu gốc)
    private ObservableList<String> allCategories = FXCollections.observableArrayList();

    // Danh sách danh mục đã lọc (hiển thị trong ComboBox)
    private ObservableList<String> filteredCategories = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Khởi tạo dữ liệu danh mục
        initializeCategoryData();

        // Thiết lập ComboBox có thể tìm kiếm
        setupSearchableComboBox();

        // Các khởi tạo khác...
    }

    private void initializeCategoryData() {
        // Thêm dữ liệu danh mục của bạn vào đây
        allCategories.addAll(
                "Bút viết",
                "Giấy in ấn",
                "Dụng cụ văn phòng",
                "Máy tính để bàn",
                "Máy in",
                "Thiết bị điện tử",
                "Đồ dùng học tập",
                "Sách vở",
                "Kệ tài liệu",
                "Ghế văn phòng",
                "Bàn làm việc",
                "Túi xách công sở",
                "Máy photocopy",
                "Scanner",
                "Máy fax"
                // Thêm các danh mục khác...
        );

        // Sao chép toàn bộ danh sách ban đầu
        filteredCategories.addAll(allCategories);
        categoryComboBox.setItems(filteredCategories);
    }

    private void setupSearchableComboBox() {
        // Thiết lập ComboBox có thể chỉnh sửa
        categoryComboBox.setEditable(true);

        // Lắng nghe sự thay đổi text trong ComboBox
        categoryComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            // Nếu ComboBox đang hiển thị dropdown, không xử lý
            if (categoryComboBox.isShowing()) return;

            Platform.runLater(() -> {
                // Lọc danh sách dựa trên text nhập vào
                filterCategories(newValue);

                // Hiển thị dropdown nếu có kết quả
                if (!filteredCategories.isEmpty()) {
                    categoryComboBox.show();
                }
            });
        });

        // Xử lý khi focus vào ComboBox
        categoryComboBox.setOnShowing(event -> {
            String currentText = categoryComboBox.getEditor().getText();
            filterCategories(currentText);
        });

        // Xử lý khi chọn một item từ dropdown
        categoryComboBox.setOnAction(event -> {
            if (categoryComboBox.getSelectionModel().getSelectedItem() != null) {
                String selectedItem = categoryComboBox.getSelectionModel().getSelectedItem();
                categoryComboBox.getEditor().setText(selectedItem);
            }
        });

        // Xử lý khi nhấn Enter
        categoryComboBox.getEditor().setOnAction(event -> {
            String text = categoryComboBox.getEditor().getText();
            if (!text.isEmpty()) {
                // Kiểm tra xem text có khớp với danh mục nào không
                boolean found = allCategories.stream()
                        .anyMatch(category -> category.toLowerCase().equals(text.toLowerCase()));

                if (!found) {
                    // Có thể thêm danh mục mới hoặc hiển thị thông báo
                    // Ví dụ: thêm danh mục mới vào danh sách
                    addNewCategory(text);
                }
            }
            categoryComboBox.hide();
        });
    }

    private void filterCategories(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            // Nếu không có text tìm kiếm, hiển thị tất cả
            filteredCategories.setAll(allCategories);
        } else {
            // Lọc danh sách dựa trên text tìm kiếm (không phân biệt hoa thường)
            String lowerCaseFilter = searchText.toLowerCase();

            ObservableList<String> filtered = allCategories.stream()
                    .filter(category -> category.toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            filteredCategories.setAll(filtered);
        }

        // Cập nhật items cho ComboBox
        categoryComboBox.setItems(filteredCategories);
    }

    private void addNewCategory(String newCategory) {
        // Kiểm tra xem danh mục đã tồn tại chưa
        boolean exists = allCategories.stream()
                .anyMatch(category -> category.toLowerCase().equals(newCategory.toLowerCase()));

        if (!exists) {
            // Thêm danh mục mới vào danh sách
            allCategories.add(newCategory);

            // Có thể lưu vào database ở đây
            // saveCategoryToDatabase(newCategory);

            // Cập nhật filtered list
            filteredCategories.setAll(allCategories);
            categoryComboBox.setItems(filteredCategories);

            // Chọn danh mục mới được thêm
            categoryComboBox.getSelectionModel().select(newCategory);
        }
    }

    // Phương thức để lấy giá trị đã chọn
    public String getSelectedCategory() {
        return categoryComboBox.getEditor().getText();
    }

    // Phương thức để set giá trị cho ComboBox
    public void setSelectedCategory(String category) {
        categoryComboBox.getEditor().setText(category);
        categoryComboBox.getSelectionModel().select(category);
    }

    // Phương thức để clear selection
    public void clearCategorySelection() {
        categoryComboBox.getEditor().clear();
        categoryComboBox.getSelectionModel().clearSelection();
    }
}