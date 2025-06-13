package model.service;

import model.dto.OrderDto;

import java.util.List;

public interface OrderService {
    void createOrder(model.dto.OrderDto orderDto);

    void createOrder(OrderDto orderDto);

    void getAllOrders();
    OrderDto getOrderByCode(String orderCode);
    boolean cancelOrder(String orderCode);
    List<OrderDto> getOrdersByUserId(String userId);
}