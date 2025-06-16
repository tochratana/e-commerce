package controller;

import model.dto.order.OrderDTO;
import model.dto.order.OrderItemDto;
import model.service.OrderService;

import java.util.List;

public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService service) {
        this.orderService = service;
    }

    // Place order from cart for currently logged-in user
    public OrderDTO placeOrder() {
        return orderService.placeOrder();
    }

    // Get all orders for a user
    public List<OrderDTO> getOrdersByUser(int userId) {
        return orderService.getAllOrdersByUser(userId);
    }

    // Get order detail by order id
    public OrderDTO getOrderDetail(int orderId) {
        return orderService.getOrderById(orderId);
    }

    public List<OrderItemDto> getAllOrderItemByUserId(int userId) {
        return orderService.getOrderItemById(userId);
    }


    // Cancel order by id
    public boolean cancelOrder(int orderId) {
        return orderService.cancelOrderById(orderId);
    }
}
