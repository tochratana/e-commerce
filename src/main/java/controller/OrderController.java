package controller;

import model.dto.OrderDto;
import model.service.OrderItemService;
import model.service.OrderService;

import java.sql.Timestamp;

public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(String orderCode, Integer userId, Double totalPrice) {
        OrderDto orderDto = new OrderDto(orderCode, userId, new Timestamp(System.currentTimeMillis()), totalPrice);
        orderService.createOrder(orderDto);

    }
    public void showAllOrders() {
        orderService.getAllOrders();
    }
}
