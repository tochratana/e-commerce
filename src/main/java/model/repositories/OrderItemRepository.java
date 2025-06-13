package model.repositories;

import model.entities.OrderItem;
import java.util.List;

public interface OrderItemRepository {
    OrderItem save(OrderItem orderItem);
    List<OrderItem> findByOrderCode(String orderCode);
    void deleteByOrderCode(String orderCode);
}