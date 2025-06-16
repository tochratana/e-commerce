package model.service;

import mapper.OrderItemMapper;
import mapper.OrderMapper;
import model.dto.order.OrderDTO;
import model.dto.order.OrderItemDto;
import model.entities.CartItem;
import model.entities.Order;
import model.entities.OrderItem;
import model.entities.Users;
import model.repositories.CartRepositoryImpl;
import model.repositories.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private final CartRepositoryImpl cartRepo;
    private final OrderRepository orderRepo;
    private final UserService userService;
    private final ProductService productService;

    public OrderServiceImpl(CartRepositoryImpl cartRepo, OrderRepository orderRepo, UserService userService, ProductService productService) {
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public OrderDTO placeOrder() {
        Users currentUser = userService.loadCurrentSession();
        if (currentUser == null) {
            throw new IllegalStateException("No user logged in.");
        }

        int userId = currentUser.getId();
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        List<CartItem> cartItems = cartRepo.getCartItemsByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        for (CartItem item : cartItems) {
            if (item == null ||
                    item.getProductId() == null ||
                    item.getProductName() == null || item.getProductName().isBlank() ||
                    item.getProductPrice() == null || item.getProductPrice() < 0 ||
                    item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid cart item: " + (item != null ? item.getProductName() : "null"));
            }
        }

        int totalQty = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();

        if (totalQty <= 0 || totalPrice <= 0) {
            throw new IllegalStateException("Invalid total quantity or price.");
        }

        List<OrderItem> orderItems = cartItems.stream().map(cart -> OrderItem.builder()
                .productId(cart.getProductId())
                .productName(cart.getProductName())
                .productPrice(cart.getProductPrice())
                .quantity(cart.getQuantity())
                .build()).collect(Collectors.toList());

        Order order = Order.builder()
                .userId(userId)
                .orderCode(UUID.randomUUID().toString())
                .totalQuantity(totalQty)
                .totalPrice(totalPrice)
                .orderDate(LocalDate.now())
                .items(orderItems)
                .build();

        Order savedOrder = orderRepo.save(order);

        cartRepo.clearCartByUserId(userId);

        return OrderMapper.toOrderDTO(savedOrder);
    }

    @Override
    public List<OrderItemDto> getOrderItemById(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive.");
        }

        List<Order> orders = orderRepo.findAllByUserId(userId);
        List<OrderItemDto> itemDtos = new ArrayList<>();

        if (orders == null || orders.isEmpty()) {
            return List.of();
        }

        for (Order order : orders) {
            for (OrderItem item : order.getItems()) { // Assuming `getItems()` returns a list of OrderItem
                itemDtos.add(OrderItemMapper.toDTO(item));
            }
        }

        return itemDtos;
    }

    @Override
    public List<OrderDTO> getAllOrdersByUser(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive.");
        }

        List<Order> orders = orderRepo.findAllByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            return List.of();
        }

        return orders.stream()
                .map(OrderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    // TODO OrderItemDto


    @Override
    public OrderDTO getOrderById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }

        Order order = orderRepo.findById(orderId);
        if (order == null) {
            throw new IllegalStateException("Order with ID " + orderId + " not found.");
        }

        return OrderMapper.toOrderDTO(order);
    }

    @Override
    public boolean cancelOrderById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }

        Order order = orderRepo.findById(orderId);
        if (order == null) {
            throw new IllegalStateException("Order with ID " + orderId + " not found.");
        }

        orderRepo.deleteById(orderId);
        return true;
    }
}
