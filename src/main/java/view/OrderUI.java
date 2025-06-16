package view;

import controller.CartController;
import controller.OrderController;
import controller.ProductController;
import model.dto.CartItemCreateDto;
import model.dto.CartItemDisplayDto;
import model.dto.order.OrderDTO;
import model.dto.order.OrderItemDto;
import model.dto.product.ProductResponseDto;
import model.entities.Cart;
import model.entities.Users;

import java.util.*;
import java.util.Scanner;
import model.service.ProductService;
import model.service.UserServiceImpl;

import static view.UIComponents.*;

public class OrderUI {
    private final ProductController productController;
    private final OrderController  controller;
    private final ProductService productService;
    private final Scanner scanner = new Scanner(System.in);
    private static  final CartController cartController = new CartController();
    UserServiceImpl userService = new UserServiceImpl();  // Only once
    public OrderUI(OrderController controller,ProductController productController,ProductService productService) {
        this.controller = controller;
        this.productController = productController;
        this.productService = productService;
    }

    public void start(int userId) {
        while (true) {
            System.out.println(completeUITable.showOrderMenuUI());
            System.out.print(">>> Choose an option (1-7): ");

            int choice = readInt("Please enter a valid number between 0 and 4: ");
            switch (choice) {
                case 1 -> addItemToCart(userId);
                case 2 -> viewCart(userId);
                case 3 -> placeOrder();
                case 4 -> viewAllOrders(userId);
                case 5 -> viewOrderDetail();
                case 6 -> cancelOrder(userId);
                case 7 -> {
                    System.out.println("Exiting Order Menu.");
                    return;
                }
                default -> System.out.println("Invalid choice! Please choose between 0-4.");
            }
        }
    }

    private void placeOrder() {
        try {
            OrderDTO order = controller.placeOrder();
            System.out.println("Order placed successfully: ID=" + order.id() + ", Code=" + order.orderCode());
            printReceipt(order);
        } catch (Exception e) {
            System.out.println("Failed to place order: " + e.getMessage());
        }

    }

    private void printReceipt(OrderDTO order) {
        System.out.println(completeUITable.printReceiptUI(order));
    }


    private void viewAllOrders(int userId) {
        List<OrderDTO> orders = controller.getOrdersByUser(userId);
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        TableUI<OrderDTO> tableUI = new TableUI<>();
        tableUI.getTableDisplay(orders);
    }


    private void viewOrderDetail() {
        int orderId = readPositiveInt("Enter order ID: ");
        OrderDTO order = controller.getOrderDetail(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        // Print order summary info
//        System.out.printf("Order Code: %s, Date: %s, Total Price: %.2f%n",
//                order.orderCode(), order.orderDate(), order.totalPrice());
        // Display order items as table
        List<OrderItemDto> items = order.items();
        if (items == null || items.isEmpty()) {
            System.out.println("No items in this order.");
            return;
        }

        TableUI<OrderItemDto> tableUI = new TableUI<>();
        tableUI.getTableDisplay(items);
        System.out.println(completeUITable.orderDetailUI(order));
    }

    private void cancelOrder(int userId) {
        List<OrderDTO> orders = controller.getOrdersByUser(userId);
        List<OrderItemDto> orderItems = controller.getAllOrderItemByUserId(userId);
        if (orders.isEmpty()) {
            System.out.println(RED+"No orders found."+RESET);
            return;
        }
        TableUI<OrderItemDto> tableUI = new TableUI<>();
        tableUI.getTableDisplay(orderItems);

        int orderId = readPositiveInt("[+] Enter order ID to cancel: ");
        System.out.print(RED+"Are you sure you want to cancel this order? (y/n): "+RESET);
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println(RED+"Order cancellation aborted."+RESET);
            return;
        }

        boolean canceled = controller.cancelOrder(orderId);
        System.out.println(canceled ? "✅ Order canceled successfully." : "❌ Order not found or could not be canceled.");
    }

    public void addItemToCart(int userId) {
        // Load currently logged-in user
        Users currentUser = userService.loadCurrentSession();
        if (currentUser == null) {
            System.out.println(RED+"\n⚠️ No user is currently logged in. Please log in first."+RESET);
            return;
        }

        userId = currentUser.getId();

        // Display products
        List<ProductResponseDto> products = productController.getAllProducts();
        view.TableUI<ProductResponseDto> tableUI = new view.TableUI<>();
        tableUI.getTableDisplay(products);

        System.out.println("\n🛒 Add Item to Cart");

        String productUUID;
        while (true) {
            System.out.print("→ Enter Product UUID: ");
            productUUID = scanner.nextLine().trim();

            try {
                UUID.fromString(productUUID); // Validate UUID format
                break; // valid UUID, exit loop
            } catch (IllegalArgumentException e) {
                System.out.println(RED+"❌ Invalid UUID format. Please enter a valid UUID."+RESET);
            }
        }

        int quantity;
        while (true) {
            System.out.print("→ Enter Quantity: ");
            String quantityInput = scanner.nextLine().trim();
            try {
                quantity = Integer.parseInt(quantityInput);
                if (quantity <= 0) {
                    System.out.println(RED+"❌ Quantity must be a positive number."+RESET);
                } else {
                    break; // valid quantity, exit loop
                }
            } catch (NumberFormatException e) {
                System.out.println(RED+"❌ Invalid quantity. Please enter a valid number."+RESET);
            }
        }

        // Create DTO and call controller
        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto(productUUID, quantity);

        // Assuming your cartController.addItemToCart returns a String message
        String result = cartController.addItemToCart(cartItemCreateDto, userId);
        System.out.println("\n✅ " + result);
    }

    public void viewCart(int userId) {
        Users currentUser = userService.loadCurrentSession();

        if (currentUser == null) {
            System.out.println(RED+"\n⚠️ No user is currently logged in. Please login first.\n"+RESET);
            return;
        }

        userId = currentUser.getId();
        List<Cart> cartItems = cartController.getCartItemsByUserId(userId);

        System.out.println("\n============================"+YELLOW + " Your Cart"+RESET+" ============================");

        if (cartItems.isEmpty()) {
            System.out.println("🛒 Your cart is empty.\n");
            return;
        }

        List<CartItemDisplayDto> cartDtoList = new ArrayList<>();
        for (Cart cart : cartItems) {
            Optional<ProductResponseDto> optionalProduct = productService.getProductById(cart.getProductId().toString());

            String productName = optionalProduct
                    .map(ProductResponseDto::getName)
                    .orElse("Unknown Product");

            String productUuid = cart.getProductId().toString(); // assuming UUID is stored as Integer or UUID

            cartDtoList.add(new CartItemDisplayDto(
                    productName,
                    productUuid,
                    cart.getQuantity()
            ));
        }

        TableUI<CartItemDisplayDto> tableUI = new TableUI<>();
        tableUI.getTableDisplay(cartDtoList);

        System.out.printf("Total items: %d\n\n", cartItems.size());

    }


    // Utility methods for input validation

    private int readInt(String errorMessage) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(errorMessage);
            }
        }
    }

    private int readPositiveInt(String prompt) {
        int number;
        do {
            System.out.print(prompt);
            number = readInt(RED+"Invalid input. Please enter a valid number: "+RESET);
            if (number <= 0) {
                System.out.println(RED+"Please enter a number greater than 0."+RESET);
            }
        } while (number <= 0);
        return number;
    }
}
