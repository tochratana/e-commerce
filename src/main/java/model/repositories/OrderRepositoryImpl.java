package model.repositories;


import model.entities.Order;
import utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public Order save(Order order) {
        String sql = "INSERT INTO orders (order_code, user_id, order_date, total_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderCode());
            stmt.setString(2, order.getUserId());
            stmt.setTimestamp(3, order.getOrderDate());
            stmt.setDouble(4, order.getTotalPrice());
            stmt.executeUpdate();
            return order;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save order", e);
        }
    }

    @Override
    public Optional<Order> findByCode(String orderCode) {
        String sql = "SELECT * FROM orders WHERE order_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Order(
                        rs.getString("order_code"),
                        rs.getString("user_id"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_price")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order", e);
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("order_code"),
                        rs.getString("user_id"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_price")
                ));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all orders", e);
        }
    }

    @Override
    public void deleteByCode(String orderCode) {
        String sql = "DELETE FROM orders WHERE order_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order", e);
        }
    }

    @Override
    public List<Order> findByUserId(String userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("order_code"),
                        rs.getString("user_id"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_price")
                ));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find orders by user", e);
        }
    }
}