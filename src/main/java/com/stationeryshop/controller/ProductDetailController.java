package com.stationeryshop.controller;

import com.stationeryshop.model.InventoryProduct;
import com.stationeryshop.utils.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class ProductDetailController {

    @FXML
    private Label categoryLbl;

    @FXML
    private Button addToCartBtn;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productNameLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label quantityLbl;

    @FXML
    private Label productIdLbl;

    @FXML
    private Button backBtn;

    @FXML
    private Label supplierLbl;

    @FXML
    private TextArea descriptionTextArea;

    private InventoryProduct product;

    private Pane parentContainer;

    public void setParentContainer(Pane parentContainer) {
        this.parentContainer = parentContainer;
    }

    private Consumer<InventoryProduct> navigationHandler;

    public void setNavigationHandler(Consumer<InventoryProduct> handler) {
        this.navigationHandler = handler;
    }

    public void initialize() {
        if(product != null) {
            setItemDetails();
        }
    }
    public void setup(InventoryProduct product) {
        System.out.println(product.getProductId());
        this.product = product;
        setItemDetails();
    }

    public ProductDetailController() {
    }


    void setItemDetails(){
        productNameLbl.setText(product.getProductName());
        categoryLbl.setText(product.getCategoryName());
        quantityLbl.setText(String.valueOf(product.getQuantity()));
        priceLbl.setText(String.valueOf(product.getProductPrice()));
        productIdLbl.setText(String.valueOf(product.getProductId()));
        supplierLbl.setText(product.getSupplierName());
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
        descriptionTextArea.setText(product.getProductDescription());
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            if (parentContainer != null) {
                // Load ShopView FXML
                final String SHOP_PATH = "/fxml/ShopView.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(SHOP_PATH));
                Pane shopViewPane = loader.load();

                // Lấy controller và thiết lập handler
                ShopViewController shopController = loader.getController();
                if (shopController != null && navigationHandler != null) {
                    // Thiết lập lại handler cho việc click item
                    shopController.setItemSelectedHandler(navigationHandler);
                }

                // Thay thế nội dung của parent container
                parentContainer.getChildren().clear();
                parentContainer.getChildren().add(shopViewPane);

                // Nếu parent là AnchorPane, set anchor constraints
                if (parentContainer instanceof javafx.scene.layout.AnchorPane) {
                    javafx.scene.layout.AnchorPane.setTopAnchor(shopViewPane, 0.0);
                    javafx.scene.layout.AnchorPane.setBottomAnchor(shopViewPane, 0.0);
                    javafx.scene.layout.AnchorPane.setLeftAnchor(shopViewPane, 0.0);
                    javafx.scene.layout.AnchorPane.setRightAnchor(shopViewPane, 0.0);
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

    @FXML
    void handleAddToCart(ActionEvent event) {
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

