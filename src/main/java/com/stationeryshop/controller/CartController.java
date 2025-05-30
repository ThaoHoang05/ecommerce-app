package com.stationeryshop.controller;

import com.stationeryshop.dao.InvoiceDAO;
import com.stationeryshop.dao.InventoryDAO;
import com.stationeryshop.dao.ProductDAO;
import com.stationeryshop.model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class CartController {

    @FXML
    private Label totalLabel;

    @FXML
    private Button checkoutBtn;

    @FXML
    private VBox cartItemsContainer;

    @FXML
    private Label itemCountLabel;

    // Giỏ hàng lưu trữ tạm thời (Map: ProductId -> Quantity)
    private Map<Integer, CartItem> cartItems;
    
    // List to keep track of item controllers
    private List<Cart_ItemController> itemControllers;
    
    // Current user and customer for checkout
    private User currentUser;
    private Customer currentCustomer;

    // Inner class để lưu thông tin item trong giỏ hàng
    public static class CartItem {
        private Product product;
        private int quantity;
        private double subtotal;
        
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
            this.subtotal = quantity * product.getPrice();
        }
        
        // Getters và Setters
        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { 
            this.quantity = quantity;
            this.subtotal = quantity * product.getPrice();
        }
        
        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }
    
    public CartController() {
        this.cartItems = new HashMap<>();
        this.itemControllers = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        // Initialize the cart display
        refreshCartDisplay();
        
        // Disable checkout button if cart is empty
        updateCheckoutButtonState();
    }
    
    /**
     * Thêm sản phẩm vào giỏ hàng
     * @param productId ID sản phẩm
     * @param quantity Số lượng cần thêm
     * @return 1: thành công, 0: vượt quá tồn kho, -1: lỗi
     */
    public int handleAddToCart(int productId, int quantity) {
        Properties prop = new Properties();
        ProductDAO productDAO;
        InventoryDAO inventoryDAO;
        
        try {
            prop.load(getClass().getResourceAsStream("/config.properties"));
            String admin = prop.getProperty("db.admin");
            String adminpass = prop.getProperty("db.adminpwd");
            productDAO = new ProductDAO(admin, adminpass);
            inventoryDAO = new InventoryDAO(admin, adminpass);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        
        try {
            // Lấy thông tin sản phẩm
            Product product = productDAO.getProductById(productId);
            if (product == null) return -1;
            
            // Kiểm tra tồn kho trong database
            int stockInDB = inventoryDAO.getStockLevel(productId);
            
            // Tính tổng số lượng hiện tại trong giỏ hàng + số lượng muốn thêm
            int currentCartQuantity = cartItems.containsKey(productId) ? cartItems.get(productId).getQuantity() : 0;
            int totalRequestedQuantity = currentCartQuantity + quantity;
            
            // Kiểm tra xem có vượt quá tồn kho không
            if (totalRequestedQuantity > stockInDB) {
                return 0; // vượt quá tồn kho
            }
            
            // Thêm vào giỏ hàng
            if (cartItems.containsKey(productId)) {
                // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
                CartItem existingItem = cartItems.get(productId);
                existingItem.setQuantity(totalRequestedQuantity);
            } else {
                // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
                CartItem newItem = new CartItem(product, quantity);
                cartItems.put(productId, newItem);
            }
            
            refreshCartDisplay();
            return 1; // thành công
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Cập nhật số lượng sản phẩm trong giỏ hàng
     * @param productId ID sản phẩm
     * @param newQuantity Số lượng mới
     * @return 1: thành công, 0: vượt quá tồn kho hoặc sản phẩm không tồn tại, -1: lỗi
     */
    public int handleUpdateQuantity(int productId, int newQuantity) {
        if (!cartItems.containsKey(productId)) {
            return 0; // sản phẩm không có trong giỏ hàng
        }
        
        if (newQuantity <= 0) {
            // Xóa sản phẩm khỏi giỏ hàng nếu số lượng <= 0
            cartItems.remove(productId);
            return 1;
        }
        
        Properties prop = new Properties();
        InventoryDAO inventoryDAO;
        
        try {
            prop.load(getClass().getResourceAsStream("/config.properties"));
            String admin = prop.getProperty("db.admin");
            String adminpass = prop.getProperty("db.adminpwd");
            inventoryDAO = new InventoryDAO(admin, adminpass);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        
        // Kiểm tra tồn kho
        int stockInDB = inventoryDAO.getStockLevel(productId);
        if (newQuantity > stockInDB) {
            return 0; // vượt quá tồn kho
        }
        
        // Cập nhật số lượng
        CartItem item = cartItems.get(productId);
        item.setQuantity(newQuantity);
        
        return 1;
    }
    
    /**
     * Xóa sản phẩm khỏi giỏ hàng
     * @param productId ID sản phẩm cần xóa
     * @return 1: thành công, 0: sản phẩm không tồn tại
     */
    public int handleRemoveFromCart(int productId) {
        if (cartItems.containsKey(productId)) {
            cartItems.remove(productId);
            return 1;
        }
        return 0;
    }
    
    /**
     * Tạo đơn hàng từ giỏ hàng và lưu vào database
     * @param user Người dùng tạo đơn
     * @param customer Khách hàng
     * @param discountAmount Số tiền giảm giá
     * @return 1: thành công, 0: giỏ hàng trống hoặc không đủ hàng, -1: lỗi
     */
    public int handleCreateOrder(User user, Customer customer, double discountAmount) {
        if (isCartEmpty()) {
            return 0; // giỏ hàng trống
        }
        
        Properties prop = new Properties();
        InvoiceDAO invoiceDAO;
        InventoryDAO inventoryDAO;
        
        try {
            prop.load(getClass().getResourceAsStream("/config.properties"));
            String admin = prop.getProperty("db.admin");
            String adminpass = prop.getProperty("db.adminpwd");
            invoiceDAO = new InvoiceDAO(admin, adminpass);
            inventoryDAO = new InventoryDAO(admin, adminpass);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        
        try {
            // Kiểm tra lại tồn kho trước khi tạo đơn
            for (CartItem item : cartItems.values()) {
                int stockInDB = inventoryDAO.getStockLevel(item.getProduct().getProductId());
                if (item.getQuantity() > stockInDB) {
                    return 0; // không đủ hàng
                }
            }
            
            // Tạo hóa đơn
            Invoice invoice = new Invoice();
            invoice.setUser(user);
            invoice.setCustomer(customer);
            invoice.setInvoiceDate(LocalDate.now());
            invoice.setTotalAmount(calculateTotalAmount());
            invoice.setDiscountAmount(discountAmount);
            invoice.setFinalAmount(calculateTotalAmount() - discountAmount);
            invoice.setStatus("completed");
            
            // Tạo danh sách chi tiết hóa đơn
            List<InvoiceDetail> details = new ArrayList<>();
            for (CartItem item : cartItems.values()) {
                InvoiceDetail detail = new InvoiceDetail();
                detail.setProduct(item.getProduct());
                detail.setQuantity(item.getQuantity());
                detail.setUnitPrice(item.getProduct().getPrice());
                detail.setSubtotal(item.getSubtotal());
                details.add(detail);
            }
            invoice.setDetails(details);
            
            // Lưu hóa đơn vào database
            boolean saved = invoiceDAO.saveInvoice(invoice);
            if (saved) {
                // Cập nhật tồn kho (trừ số lượng thực tế)
                for (CartItem item : cartItems.values()) {
                    inventoryDAO.updateStock(item.getProduct().getProductId(), -item.getQuantity());
                }
                
                // Xóa giỏ hàng sau khi đặt hàng thành công
                clearCart();
                return 1; // thành công
            } else {
                return -1; // lỗi khi lưu
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Load and display all cart items
     */
    private void loadCartItems() {
        // Clear existing items
        cartItemsContainer.getChildren().clear();
        itemControllers.clear();
        
        List<CartItem> cartItemsList = getCartItems();
        
        for (CartItem cartItem : cartItemsList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Cart_Item.fxml"));
                HBox cartItemNode = loader.load();

                Cart_ItemController controller = loader.getController();
                controller.initData(cartItem, this);
                
                itemControllers.add(controller);
                cartItemsContainer.getChildren().add(cartItemNode);
                
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("Lỗi", "Không thể tải giao diện item giỏ hàng");
            }
        }
    }

    @FXML
    void continueShopping(ActionEvent event) {
        // Logic to navigate back to product catalog
        // This depends on your navigation system
        try {
            // Example navigation - adjust path according to your FXML structure
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProductCatalog.fxml"));
            Parent root = loader.load();
            // Navigation logic here - this depends on your application structure
            // You might want to use Scene switching or hide/show panels
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Lỗi", "Không thể quay lại trang sản phẩm");
        }
    }

    @FXML
    void proceedToCheckout(ActionEvent event) {
        if (isCartEmpty()) {
            showErrorAlert("Giỏ hàng trống", "Vui lòng thêm sản phẩm vào giỏ hàng trước khi thanh toán");
            return;
        }
        
        // Show checkout confirmation dialog
        boolean confirmed = showConfirmDialog(
            "Xác nhận thanh toán", 
            String.format("Tổng tiền: %.2f VND\nBạn có chắc chắn muốn thanh toán?", calculateTotalAmount())
        );
        
        if (confirmed) {
            // For now, create order with no discount
            // You might want to add discount input dialog here
            double discountAmount = 0.0;
            
            // Use default user and customer - in real app, these should be set properly
            if (currentUser == null || currentCustomer == null) {
                showErrorAlert("Lỗi", "Thông tin người dùng và khách hàng chưa được thiết lập");
                return;
            }
            
            int result = handleCreateOrder(currentUser, currentCustomer, discountAmount);
            
            switch (result) {
                case 1: // Success
                    showSuccessAlert("Thành công", "Đơn hàng đã được tạo thành công!");
                    refreshCartDisplay(); // This will clear the display since cart is now empty
                    break;
                case 0: // Empty cart or insufficient stock
                    showErrorAlert("Không thể tạo đơn", "Giỏ hàng trống hoặc không đủ hàng trong kho");
                    break;
                case -1: // Error
                    showErrorAlert("Lỗi", "Có lỗi xảy ra khi tạo đơn hàng");
                    break;
            }
        }
    }

    /**
     * Update total amount display
     */
    public void updateTotal() {
        double total = calculateTotalAmount();
        totalLabel.setText(String.format("%.2f VND", total));
        
        // Update item count
        int itemCount = getCartItemCount();
        int totalQuantity = getTotalQuantity();
        itemCountLabel.setText(String.format("%d sản phẩm (%d items)", itemCount, totalQuantity));
        
        // Update checkout button state
        updateCheckoutButtonState();
    }
    
    /**
     * Refresh the entire cart display
     */
    public void refreshCartDisplay() {
        loadCartItems();
        updateTotal();
    }
    
    /**
     * Update checkout button enabled/disabled state
     */
    private void updateCheckoutButtonState() {
        checkoutBtn.setDisable(isCartEmpty());
    }
    
    /**
     * Add product to cart (called from other controllers)
     */
    public void addProductToCart(int productId, int quantity) {
        int result = handleAddToCart(productId, quantity);
        
        switch (result) {
            case 1: // Success
                showSuccessAlert("Thành công", "Đã thêm sản phẩm vào giỏ hàng");
                break;
            case 0: // Out of stock
                showErrorAlert("Không đủ hàng", "Số lượng yêu cầu vượt quá tồn kho hiện có");
                break;
            case -1: // Error
                showErrorAlert("Lỗi", "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng");
                break;
        }
    }
    
    /**
     * Lấy danh sách các item trong giỏ hàng
     * @return List các CartItem
     */
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());
    }
    
    /**
     * Tính tổng tiền giỏ hàng
     * @return tổng tiền
     */
    public double calculateTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems.values()) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    /**
     * Kiểm tra giỏ hàng có trống không
     * @return true nếu giỏ hàng trống
     */
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
    
    /**
     * Lấy số lượng items trong giỏ hàng
     * @return số lượng items
     */
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    /**
     * Lấy tổng số lượng sản phẩm trong giỏ hàng
     * @return tổng số lượng
     */
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : cartItems.values()) {
            total += item.getQuantity();
        }
        return total;
    }
    
    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart() {
        cartItems.clear();
        refreshCartDisplay();
    }
    
    /**
     * Clear entire cart with confirmation
     */
    public void clearCartWithConfirmation() {
        boolean confirmed = showConfirmDialog(
            "Xác nhận xóa", 
            "Bạn có chắc chắn muốn xóa toàn bộ giỏ hàng?"
        );
        
        if (confirmed) {
            clearCart();
            showSuccessAlert("Thành công", "Đã xóa toàn bộ giỏ hàng");
        }
    }
    
    /**
     * Set current user for checkout
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Set current customer for checkout
     */
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }
    
    /**
     * Get cart summary information
     */
    public String getCartSummary() {
        if (isCartEmpty()) {
            return "Giỏ hàng trống";
        }
        
        return String.format("Giỏ hàng: %d sản phẩm - Tổng: %.2f VND", 
            getCartItemCount(), 
            calculateTotalAmount());
    }
    
    /**
     * Hiển thị thông báo lỗi sử dụng JavaFX Alert
     * @param title Tiêu đề
     * @param message Nội dung thông báo
     */
    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Hiển thị thông báo thành công sử dụng JavaFX Alert
     * @param title Tiêu đề
     * @param message Nội dung thông báo
     */
    public void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Hiển thị hộp thoại xác nhận sử dụng JavaFX Alert
     * @param title Tiêu đề
     * @param message Nội dung thông báo
     * @return true nếu người dùng chọn OK, false nếu chọn Cancel
     */
    public boolean showConfirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
