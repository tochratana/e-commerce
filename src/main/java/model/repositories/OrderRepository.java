package model.repositories;

import model.entities.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByCode(String orderCode);
    List<Order> findAll();
    void deleteByCode(String orderCode);
    List<Order> findByUserId(String userId);
}