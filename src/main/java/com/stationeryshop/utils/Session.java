package com.stationeryshop.utils;

import com.stationeryshop.controller.CartController;
import com.stationeryshop.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    public static User currentUser = new User();

    // Thread-safe cart storage - Thay đổi từ HashMap thành ConcurrentHashMap để thread-safe
    public static Map<Integer, Integer> cart = new ConcurrentHashMap<>();

    // Reference đến CartController để có thể cập nhật UI
    public static CartController cartController;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    public static String getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public static void clear() {
        currentUser = null;
        clearCart();
    }

    // Phương thức logout - kết hợp clear user và cart
    public static void logout() {
        currentUser = null;
        clearCart();
    }

    // Set CartController reference
    public static void setCartController(CartController controller) {
        cartController = controller;
    }

    // Get CartController reference
    public static CartController getCartController() {
        return cartController;
    }

    // Thêm sản phẩm vào cart với quantity mặc định là 1
    public static void addToCart(int productId) {
        addToCart(productId, 1);
    }

    // Thêm sản phẩm vào cart với quantity cụ thể
    public static void addToCart(int productId, int quantity) {
        if (cart.containsKey(productId)) {
            // Nếu sản phẩm đã có trong cart, tăng quantity
            int currentQuantity = cart.get(productId);
            cart.put(productId, currentQuantity + quantity);
        } else {
            // Nếu chưa có, thêm mới
            cart.put(productId, quantity);
        }

        // Cập nhật CartController nếu có
        syncCartWithController();
    }

    // Cập nhật quantity của sản phẩm trong cart
    public static void updateCartQuantity(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(productId);
        } else {
            cart.put(productId, newQuantity);
        }

        // Cập nhật CartController nếu có
        syncCartWithController();
    }

    // Set quantity cụ thể cho sản phẩm (không cộng dồn)
    public static void setCartQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, quantity);
        }

        // Cập nhật CartController nếu có
        syncCartWithController();
    }

    // Xóa sản phẩm khỏi cart
    public static void removeFromCart(int productId) {
        cart.remove(productId);

        // Cập nhật CartController nếu có
        syncCartWithController();
    }

    // Xóa toàn bộ cart
    public static void clearCart(){
        cart.clear();

        // Cập nhật CartController nếu có
        syncCartWithController();
    }

    // Lấy quantity của sản phẩm trong cart
    public static int getCartQuantity(int productId) {
        return cart.getOrDefault(productId, 0);
    }

    // Kiểm tra sản phẩm có trong cart không
    public static boolean isInCart(int productId) {
        return cart.containsKey(productId);
    }

    // Lấy tổng số items trong cart (số loại sản phẩm khác nhau)
    public static int getCartItemCount() {
        return cart.size();
    }

    // Lấy size của cart (alias cho getCartItemCount)
    public static int getCartSize() {
        return cart.size();
    }

    // Lấy tổng quantity trong cart (tổng số lượng tất cả sản phẩm)
    public static int getTotalQuantity() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Lấy tổng cart items (alias cho getTotalQuantity)
    public static int getTotalCartItems() {
        return cart.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Kiểm tra cart có trống không
    public static boolean isCartEmpty() {
        return cart.isEmpty();
    }

    // Lấy toàn bộ cart
    public static Map<Integer, Integer> getCart() {
        return new HashMap<>(cart);
    }

    // Utility method để sync cart với CartController
    public static void syncCartWithController() {
        if (cartController != null) {
            cartController.syncWithSession();
        }
    }

    // Debug method để in nội dung cart
    public static void printCart() {
        System.out.println("=== Cart Contents ===");
        if (cart.isEmpty()) {
            System.out.println("Cart is empty");
        } else {
            cart.forEach((productId, quantity) -> 
                System.out.println("Product ID: " + productId + ", Quantity: " + quantity));
        }
        System.out.println("Total items: " + getTotalCartItems());
        System.out.println("==================");
    }
}
