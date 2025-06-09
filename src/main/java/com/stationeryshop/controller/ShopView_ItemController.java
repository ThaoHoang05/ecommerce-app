package com.stationeryshop.controller;

import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class ShopView_ItemController {
    @FXML
    private Label categoryLbl;

    @FXML
    private Button addToCartButton;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productNameLbl;

    @FXML
    private Label productPriceLbl;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Label productSupplierLbl;

    private InventoryProduct product;

    public ShopView_ItemController() {
        // Constructor mặc định để FXML có thể tạo instance
    }

    public ShopView_ItemController(InventoryProduct product) {
        this.product = product;
    }

    // Setter để thiết lập product từ bên ngoài
    public void setProduct(InventoryProduct product) {
        this.product = product;
        setData(); // Tự động cập nhật UI khi set product
    }

    @FXML
    void addToCartOnPressed(ActionEvent event) {
        System.out.println(Session.getCurrentUser().getUsername());
        if(Session.getCurrentUsername() != null){
        if (product == null) {
            showErrorAlert("Lỗi", "Thông tin sản phẩm không hợp lệ");
            return;
        }

        if (product.getQuantity() <= 0) {
            showErrorAlert("Hết hàng", "Sản phẩm này hiện đã hết hàng");
            return;
        }

        // Hiển thị dialog để chọn số lượng
        int quantityToAdd = showQuantityInputDialog();

        if (quantityToAdd > 0) {
            // Kiểm tra số lượng có vượt quá tồn kho không
            int currentInCart = Session.getCartQuantity(product.getProductId());
            int totalRequested = currentInCart + quantityToAdd;

            if (totalRequested > product.getQuantity()) {
                showErrorAlert("Không đủ hàng",
                        String.format("Chỉ còn %d sản phẩm trong kho (đã có %d trong giỏ hàng)",
                                product.getQuantity(), currentInCart));
                return;
            }

            // Thêm vào Session cart
            Session.addToCart(product.getProductId(), quantityToAdd);

            showSuccessAlert("Thành công",
                    String.format("Đã thêm %d %s vào giỏ hàng", quantityToAdd, product.getProductName()));
        }}
        else{
            showErrorAlert("Chưa đăng nhập", "Xin hãy đăng nhập rồi mới thêm vào giỏ hàng");
        }
    }

    @FXML
    void viewDetailsOnPressed(ActionEvent event) {
        // Implement product details view
        if (product != null) {
            showProductDetails();
        }
    }

    public void setData(){
        if (product == null) return;

        categoryLbl.setText(product.getCategoryName());
        productNameLbl.setText(product.getProductName());
        productPriceLbl.setText(String.format("%.2f VND", product.getProductPrice()));
        productSupplierLbl.setText(product.getSupplierName());
        try {
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                String url = product.getImageUrl();
                if (url != null && url.startsWith("C:\\")) {
                    url = "file:/" + url.replace("\\", "/"); // Chuyển đổi sang định dạng file URL hợp lệ
                }
                productImage.setImage(new Image(url));
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi để kiểm tra
        }


        // Disable button nếu hết hàng
        if(product.getQuantity() == 0) {
            addToCartButton.setDisable(true);
            addToCartButton.setText("Hết hàng");
        } else {
            addToCartButton.setDisable(false);
            addToCartButton.setText("Thêm vào giỏ");
        }
    }

    /**
     * Hiển thị dialog để nhập số lượng
     */
    private int showQuantityInputDialog() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Chọn số lượng");
        dialog.setHeaderText("Thêm sản phẩm vào giỏ hàng");
        dialog.setContentText(String.format("Nhập số lượng (tối đa %d):", product.getQuantity()));

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                int quantity = Integer.parseInt(result.get());
                if (quantity > 0 && quantity <= product.getQuantity()) {
                    return quantity;
                } else {
                    showErrorAlert("Số lượng không hợp lệ",
                            String.format("Vui lòng nhập số lượng từ 1 đến %d", product.getQuantity()));
                }
            } catch (NumberFormatException e) {
                showErrorAlert("Lỗi định dạng", "Vui lòng nhập một số nguyên hợp lệ");
            }
        }

        return 0; // Không thêm gì vào giỏ hàng
    }

    /**
     * Hiển thị chi tiết sản phẩm
     */
    private void showProductDetails() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết sản phẩm");
        alert.setHeaderText(product.getProductName());

        String details = String.format(
                "Danh mục: %s\n" +
                        "Nhà cung cấp: %s\n" +
                        "Giá: %.2f VND\n" +
                        "Còn lại: %d sản phẩm\n" +
                        "Trong giỏ hàng: %d sản phẩm",
                product.getCategoryName(),
                product.getSupplierName(),
                product.getProductPrice(),
                product.getQuantity(),
                Session.getCartQuantity(product.getProductId())
        );

        alert.setContentText(details);
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