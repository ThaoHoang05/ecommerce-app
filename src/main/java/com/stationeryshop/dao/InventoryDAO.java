package dao;

import model.InventoryItem;
import model.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    private Connection conn;

    public InventoryDAO(Connection conn) {
        this.conn = conn;
    }

    public void updateStock(int productId, int quantityChange) throws SQLException {
        String sql = "UPDATE inventory SET quantity_on_hand = quantity_on_hand + ?, last_stocked_date = ? WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityChange);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        }
    }

    public int getStockLevel(int productId) throws SQLException {
        String sql = "SELECT quantity_on_hand FROM inventory WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity_on_hand");
            }
        }
        return 0;
    }

    public List<InventoryItem> getLowStockItems(int threshold) throws SQLException {
        List<InventoryItem> list = new ArrayList<>();
        String sql = "SELECT i.*, p.product_name FROM inventory i " +
                     "JOIN products p ON i.product_id = p.product_id " +
                     "WHERE quantity_on_hand <= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));

                InventoryItem item = new InventoryItem(
                    rs.getInt("inventory_id"),
                    product,
                    rs.getInt("quantity_on_hand"),
                    rs.getDate("last_stocked_date").toLocalDate()
                );
                list.add(item);
            }
        }
        return list;
    }
}
