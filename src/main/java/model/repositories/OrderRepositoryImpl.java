package model.repositories;

import model.entities.Order;
import model.entities.OrderItem;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepositoryImpl implements OrderRepository {

    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    @Override
    public Order save(Order order) {
        String insertOrderSQL = """
                                    INSERT INTO orders (user_id, order_code, total_quantity, total_price, order_date) 
                                    VALUES (?, ?, ?, ?, ?)
                                """;
        String insertItemSQL = """
                                    INSERT INTO order_items (order_id, product_id, product_name, product_price, quantity) 
                                    VALUES (?, ?, ?, ?, ?)
                                """;

        Connection conn = null;

        try {
            conn = databaseConfig.getDatabaseConnection();
            if (conn == null) {
                throw new SQLException("Connection is null");
            }
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getUserId());
                orderStmt.setString(2, order.getOrderCode());
                orderStmt.setInt(3, order.getTotalQuantity());
                orderStmt.setDouble(4, order.getTotalPrice());
                orderStmt.setDate(5, java.sql.Date.valueOf(order.getOrderDate()));

                int affectedRows = orderStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);
                        order.setId(orderId);

                        try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {
                            for (OrderItem item : order.getItems()) {
                                itemStmt.setInt(1, orderId);
                                itemStmt.setObject(2, item.getProductId());  // FIXED here: use setObject for UUID
                                itemStmt.setString(3, item.getProductName());
                                itemStmt.setDouble(4, item.getProductPrice());
                                itemStmt.setInt(5, item.getQuantity());
                                itemStmt.addBatch();
                            }
                            itemStmt.executeBatch();
                        }

                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            conn.commit();
            return order;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Order> findAllByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId: " + userId);
        }
        String sqlOrder = "SELECT * FROM orders WHERE user_id = ?";
        String sqlItems = "SELECT * FROM order_items WHERE order_id = ?";

        List<Order> orders = new ArrayList<>();

        try (Connection conn = databaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new SQLException("Connection is null");
            }

            try (PreparedStatement orderStmt = conn.prepareStatement(sqlOrder);
                 PreparedStatement itemStmt = conn.prepareStatement(sqlItems)) {

                orderStmt.setInt(1, userId);
                try (ResultSet rsOrder = orderStmt.executeQuery()) {

                    while (rsOrder.next()) {
                        Order order = new Order();
                        order.setId(rsOrder.getInt("id"));
                        order.setUserId(rsOrder.getInt("user_id"));
                        order.setOrderCode(rsOrder.getString("order_code"));
                        order.setTotalQuantity(rsOrder.getInt("total_quantity"));
                        order.setTotalPrice(rsOrder.getDouble("total_price"));
                        order.setOrderDate(rsOrder.getDate("order_date").toLocalDate());

                        itemStmt.setInt(1, order.getId());
                        try (ResultSet rsItems = itemStmt.executeQuery()) {
                            List<OrderItem> items = new ArrayList<>();
                            while (rsItems.next()) {
                                OrderItem item = new OrderItem();
                                item.setId(rsItems.getInt("id"));
                                item.setOrderId(rsItems.getInt("order_id"));

                                // Fix: read UUID as String, then convert
                                String productIdStr = rsItems.getString("product_id");
                                UUID productId = UUID.fromString(productIdStr);
                                item.setProductId(productId);

                                item.setProductName(rsItems.getString("product_name"));
                                item.setProductPrice(rsItems.getDouble("product_price"));
                                item.setQuantity(rsItems.getInt("quantity"));
                                items.add(item);
                            }
                            order.setItems(items);
                        }

                        orders.add(order);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order findById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Invalid orderId: " + orderId);
        }

        String sqlOrder = "SELECT * FROM orders WHERE id = ?";
        String sqlItems = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = databaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new SQLException("Connection is null");
            }

            try (PreparedStatement orderStmt = conn.prepareStatement(sqlOrder);
                 PreparedStatement itemStmt = conn.prepareStatement(sqlItems)) {

                orderStmt.setInt(1, orderId);
                try (ResultSet rsOrder = orderStmt.executeQuery()) {
                    if (rsOrder.next()) {
                        Order order = new Order();
                        order.setId(rsOrder.getInt("id"));
                        order.setUserId(rsOrder.getInt("user_id"));
                        order.setOrderCode(rsOrder.getString("order_code"));
                        order.setTotalQuantity(rsOrder.getInt("total_quantity"));
                        order.setTotalPrice(rsOrder.getDouble("total_price"));
                        order.setOrderDate(rsOrder.getDate("order_date").toLocalDate());

                        itemStmt.setInt(1, orderId);
                        try (ResultSet rsItems = itemStmt.executeQuery()) {
                            List<OrderItem> items = new ArrayList<>();
                            while (rsItems.next()) {
                                OrderItem item = new OrderItem();
                                item.setId(rsItems.getInt("id"));
                                item.setOrderId(rsItems.getInt("order_id"));

                                String productIdStr = rsItems.getString("product_id");
                                UUID productId = UUID.fromString(productIdStr);
                                item.setProductId(productId);

                                item.setProductName(rsItems.getString("product_name"));
                                item.setProductPrice(rsItems.getDouble("product_price"));
                                item.setQuantity(rsItems.getInt("quantity"));
                                items.add(item);
                            }
                            order.setItems(items);
                        }

                        return order;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Invalid orderId: " + orderId);
        }
        String sqlDeleteItems = "DELETE FROM order_items WHERE order_id = ?";
        String sqlDeleteOrder = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = databaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new SQLException("Connection is null");
            }
            conn.setAutoCommit(false);

            try (PreparedStatement deleteItemsStmt = conn.prepareStatement(sqlDeleteItems);
                 PreparedStatement deleteOrderStmt = conn.prepareStatement(sqlDeleteOrder)) {

                deleteItemsStmt.setInt(1, orderId);
                deleteItemsStmt.executeUpdate();

                deleteOrderStmt.setInt(1, orderId);
                deleteOrderStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
