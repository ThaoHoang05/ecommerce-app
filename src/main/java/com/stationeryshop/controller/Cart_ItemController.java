package com.stationeryshop.controller;

import com.stationeryshop.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

public class Cart_ItemController {

    @FXML
    private Label productSupplier;

    @FXML
    private ImageView productImage;

    @FXML
    private Button decreaseBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private Label subtotalLabel;

    @FXML
    private TextField quantityField;

    @FXML
    private Button increaseBtn;

    @FXML
    private Label productName;

    @FXML
    private Label productDescription;

    @FXML
    private Label productPrice;

    @FXML
    private Label productCategory;

    // Reference to the main cart controller and cart item
    private CartController parentController;
    private CartController.CartItem cartItem;
    private int productId;

    /**
     * Initialize the cart item with data
     */
    public void initData(CartController.CartItem cartItem, CartController parentController) {
        this.cartItem = cartItem;
        this.parentController = parentController;
        this.productId = cartItem.getProduct().getProductId();
        
        // Populate UI elements with product data
        Product product = cartItem.getProduct();    
        productName.setText(product.getProductName());
        productDescription.setText(product.getDescription());
        productPrice.setText(String.format("%.2f VND", product.getPrice()));
        productCategory.setText(product.getCategory().getCategoryName());
        // Removed supplier line as Product model doesn't have supplier property
        quantityField.setText(String.valueOf(cartItem.getQuantity()));

        // Set product image if available
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(product.getImageUrl()));
                productImage.setImage(image);
            } catch (Exception e) {
        // Use default image if product image not found
        productImage.setImage(new Image(getClass().getResourceAsStream("/images/default-product.png")));
            }
        }

    updateSubtotal();
        
        // Add listener to quantity field for manual input
        quantityField.textProperty().addListener((_, _, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Handle quantity field focus lost (when user finishes typing)
        quantityField.focusedProperty().addListener((_, _, newValue) -> {
            if (!newValue) { // Focus lost
                handleQuantityFieldChange();
            }
        });

        // Handle Enter key in quantity field
        quantityField.setOnAction(_ -> handleQuantityFieldChange());
    }

    @FXML
    void decreaseQuantity(ActionEvent event) {
        int currentQuantity = cartItem.getQuantity();
        if (currentQuantity > 1) {
            updateQuantity(currentQuantity - 1);
        }
    }

    @FXML
    void increaseQuantity(ActionEvent event) {
        int currentQuantity = cartItem.getQuantity();
        updateQuantity(currentQuantity + 1);
    }

    @FXML
    void removeItem(ActionEvent event) {
        if (parentController != null) {
            boolean confirmed = parentController.showConfirmDialog(
                "Xác nhận xóa", 
                "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?"
            );
            
            if (confirmed) {
                int result = parentController.handleRemoveFromCart(productId);
                if (result == 1) {
                    parentController.refreshCartDisplay();
                    parentController.showSuccessAlert("Thành công", "Đã xóa sản phẩm khỏi giỏ hàng");
                } else {
                    parentController.showErrorAlert("Lỗi", "Không thể xóa sản phẩm khỏi giỏ hàng");
                }
            }
        }
    }
    
    /**
     * Handle quantity field change when user types manually
     */
    private void handleQuantityFieldChange() {
        try {
            String text = quantityField.getText().trim();
            if (text.isEmpty()) {
                quantityField.setText("1");
                return;
            }
            
            int newQuantity = Integer.parseInt(text);
            if (newQuantity <= 0) {
                quantityField.setText("1");
                newQuantity = 1;
            }
            
            updateQuantity(newQuantity);
        } catch (NumberFormatException e) {
            quantityField.setText(String.valueOf(cartItem.getQuantity()));
        }
    }
    
    /**
     * Update quantity in the cart
     */
    private void updateQuantity(int newQuantity) {
        if (parentController != null) {
            int result = parentController.handleUpdateQuantity(productId, newQuantity);
            
            switch (result) {
                case 1: // Success
                    cartItem.setQuantity(newQuantity);
                    quantityField.setText(String.valueOf(newQuantity));
                    updateSubtotal();
                    parentController.updateTotal();
                    break;
                case 0: // Out of stock or invalid
                    parentController.showErrorAlert(
                        "Không đủ hàng", 
                        "Số lượng yêu cầu vượt quá tồn kho hiện có"
                    );
                    quantityField.setText(String.valueOf(cartItem.getQuantity()));
                    break;
                case -1: // Error
                    parentController.showErrorAlert(
                        "Lỗi", 
                        "Có lỗi xảy ra khi cập nhật số lượng"
                    );
                    quantityField.setText(String.valueOf(cartItem.getQuantity()));
                    break;
            }
        }
    }
    
    /**
     * Update subtotal display
     */
    private void updateSubtotal() {
        double subtotal = cartItem.getSubtotal();
        subtotalLabel.setText(String.format("%.2f VND", subtotal));
    }
    
    /**
     * Get the current cart item
     */
    public CartController.CartItem getCartItem() {
        return cartItem;
    }
    
    /**
     * Refresh the display with updated data
     */
    public void refreshDisplay() {
        if (cartItem != null) {
            quantityField.setText(String.valueOf(cartItem.getQuantity()));
            updateSubtotal();
        }
    }
}
