package model.repositories;

import model.entities.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);           // Insert order and order items
    List<Order> findAllByUserId(int userId);
    Order findById(int orderId);
    void deleteById(int orderId);
}
