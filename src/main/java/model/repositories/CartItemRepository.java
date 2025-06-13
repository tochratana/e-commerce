package model.repositories;

import model.entities.CartItem;
import utils.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CartItemRepository {
    public void add(CartItem item) throws SQLException {
        String sql = """
                INSERT INTO cart_items (product_uuid, product_name, price, quantity) 
                VALUES (?, ?, ?, ?) 
                """;
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, item.getProductUuid());
                ps.setString(2, item.getProductName());
                ps.setDouble(3, item.getPrice());
                ps.setInt(4, item.getQuantity());
                ps.executeUpdate();
            }
        }
    }

    public void remove(String uuid) throws SQLException {
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            String query = """
                            DELETE FROM cart_items WHERE product_uuid = ?
                        """;
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, uuid);
                ps.executeUpdate();
            }
        }
    }

    public List<CartItem> getAll() throws SQLException {
        List<CartItem> cartItems = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            String query = """
                            SELECT * FROM cart_items
                        """;
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    CartItem item = new CartItem(
                            rs.getString("product_uuid"),
                            rs.getString("product_name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    );
                    cartItems.add(item);
                }
            }
        }
        return cartItems;
    }

    public void clear() throws SQLException {
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            String query = """
                        DELETE FROM cart_items
                        """;
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.executeUpdate();
            }
        }
    }

    public boolean isEmpty() throws SQLException {
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            String query = """
                        SELECT COUNT(*) FROM cart_items
                        """;
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return true;
    }

    public double getTotal() throws SQLException {
        double total = 0;
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            String query = """
                        SELECT SUM(price * quantity) as total FROM cart_items
                        """;
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        }
        return total;
    }
}