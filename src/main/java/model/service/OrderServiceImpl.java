package model.service;

import model.dto.OrderDto;
import model.dto.OrderItemDto;
import model.entities.Order;
import model.repositories.OrderRepository;
import service.OrderItemService;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Override
    public void createOrder(OrderDto orderDto) {
        String orderCode = "ORD-" + UUID.randomUUID().toString();
        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setUserId(orderDto.userId());
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setTotalPrice(calculateTotalPrice(orderDto.items()));

        orderRepository.save(order);

        for (OrderItemDto itemDto : orderDto.items()) {
            OrderItemDto newItem = new OrderItemDto(
                    "ITEM-" + UUID.randomUUID().toString(),
                    orderCode,
                    itemDto.productId(),
                    itemDto.quantity(),
                    itemDto.price()
            );
            orderItemService.addItemToOrder(newItem);
        }
    }

    @Override
    public void getAllOrders() {
        orderRepository.findAll().forEach(order -> {
            List<OrderItemDto> items = orderItemService.getItemsByOrderCode(order.getOrderCode());
            System.out.println("\nOrder: " + order.getOrderCode());
            System.out.println("User: " + order.getUserId());
            System.out.println("Date: " + order.getOrderDate());
            System.out.println("Total: $" + order.getTotalPrice());
            items.forEach(item -> System.out.printf(" - Product %d: %d x $%.2f%n",
                    item.productId(), item.quantity(), item.price()));
        });
    }

    @Override
    public OrderDto getOrderByCode(String orderCode) {
        return orderRepository.findByCode(orderCode)
                .map(order -> {
                    List<OrderItemDto> items = orderItemService.getItemsByOrderCode(orderCode);
                    return new OrderDto(
                            order.getOrderCode(),
                            order.getUserId(),
                            order.getOrderDate(),
                            order.getTotalPrice(),
                            items
                    );
                })
                .orElse(null);
    }

    @Override
    public boolean cancelOrder(String orderCode) {
        try {
            orderItemService.getItemsByOrderCode(orderCode)
                    .forEach(item -> orderItemService.deleteItem(item.itemCode()));
            orderRepository.deleteByCode(orderCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(order -> {
                    List<OrderItemDto> items = orderItemService.getItemsByOrderCode(order.getOrderCode());
                    return new OrderDto(
                            order.getOrderCode(),
                            order.getUserId(),
                            order.getOrderDate(),
                            order.getTotalPrice(),
                            items
                    );
                })
                .collect(Collectors.toList());
    }

    private double calculateTotalPrice(List<OrderItemDto> items) {
        return items.stream()
                .mapToDouble(item -> item.price() * item.quantity())
                .sum();
    }
}