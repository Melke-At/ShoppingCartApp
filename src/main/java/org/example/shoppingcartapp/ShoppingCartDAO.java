package org.example.shoppingcartapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ShoppingCartDAO {

    private final Connection conn;

    public ShoppingCartDAO() {
        try {
            this.conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    // ---------------------------
    // SAVE CART RECORD
    // ---------------------------
    public int saveCartRecord(int totalItems, double totalCost, String language) {
        String sql = """
            INSERT INTO cart_records (total_items, total_cost, language)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, totalItems);
            stmt.setDouble(2, totalCost);
            stmt.setString(3, language);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // return generated cart_record_id
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save cart record", e);
        }

        return -1;
    }

    // ---------------------------
    // SAVE CART ITEM
    // ---------------------------
    public void saveCartItem(int cartRecordId, int itemNumber, double price, int quantity) {
        String sql = """
            INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartRecordId);
            stmt.setInt(2, itemNumber);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, price * quantity);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save cart item", e);
        }
    }

    // ---------------------------
    // FETCH ITEMS FOR A CART
    // ---------------------------
    public List<CartItem> getItemsForCart(int cartRecordId) {
        List<CartItem> items = new ArrayList<>();

        String sql = "SELECT item_number, price, quantity, subtotal FROM cart_items WHERE cart_record_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartRecordId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(new CartItem(
                        rs.getInt("item_number"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getDouble("subtotal")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch cart items", e);
        }

        return items;
    }

    // ---------------------------
    // DATA CLASS
    // ---------------------------
    public static class CartItem {
        private final int itemNumber;
        private final double price;
        private final int quantity;
        private final double subtotal;

        public CartItem(int itemNumber, double price, int quantity, double subtotal) {
            this.itemNumber = itemNumber;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public int getItemNumber() { return itemNumber; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return subtotal; }
    }
}
