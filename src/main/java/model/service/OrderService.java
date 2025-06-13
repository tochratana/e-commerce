package model.service;

import model.dto.order.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder();
    List<OrderDTO> getAllOrdersByUser(int userId);
    OrderDTO getOrderById(int orderId);
    boolean cancelOrderById(int orderId);
}
