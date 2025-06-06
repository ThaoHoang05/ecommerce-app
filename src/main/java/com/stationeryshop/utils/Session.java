package com.stationeryshop.utils;

import com.stationeryshop.controller.CartController;
import com.stationeryshop.model.User;

import java.util.HashMap;
import java.util.Map;

public class Session {
    public static User currentUser = new User();

    // Thay đổi từ ArrayList<Integer> thành Map<Integer, Integer> để lưu productId và quantity
    public static Map<Integer, Integer> cart = new HashMap<>();

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
    }

    // Set CartController reference
    public static void setCartController(CartController controller) {
        cartController = controller;
    }

    // Thêm sản phẩm vào cart với quantity mặc định là 1
    public static void addToCart(int productId) {
        addToCart(productId, 1);
    }

    // Thêm sản phẩm vào cart với quantity cụ thể
    public static void addToCart(int productId, int quantity) {
        if (cart.containsKey(productId)) {
            // Nếu sản phẩm đã có trong cart, tăng quantity
            cart.put(productId, cart.get(productId) + quantity);
        } else {
            // Nếu chưa có, thêm mới
            cart.put(productId, quantity);
        }

        // Cập nhật CartController nếu có
        if (cartController != null) {
            cartController.syncWithSession();
        }
    }

    // Xóa sản phẩm khỏi cart
    public static void removeFromCart(int productId) {
        cart.remove(productId);

        // Cập nhật CartController nếu có
        if (cartController != null) {
            cartController.syncWithSession();
        }
    }

    // Cập nhật quantity của sản phẩm trong cart
    public static void updateCartQuantity(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(productId);
        } else {
            cart.put(productId, newQuantity);
        }

        // Cập nhật CartController nếu có
        if (cartController != null) {
            cartController.syncWithSession();
        }
    }

    // Xóa toàn bộ cart
    public static void clearCart(){
        cart.clear();

        // Cập nhật CartController nếu có
        if (cartController != null) {
            cartController.syncWithSession();
        }
    }

    // Lấy quantity của sản phẩm trong cart
    public static int getCartQuantity(int productId) {
        return cart.getOrDefault(productId, 0);
    }

    // Lấy tổng số items trong cart
    public static int getCartItemCount() {
        return cart.size();
    }

    // Lấy tổng quantity trong cart
    public static int getTotalQuantity() {
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
}