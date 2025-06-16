package view;

import controller.CartController;
import controller.OrderController;
import controller.ProductController;
import model.dto.CartItemCreateDto;
import model.dto.order.OrderDTO;
import model.dto.order.OrderItemDto;
import model.dto.product.ProductResponseDto;
import model.entities.Cart;
import model.entities.Users;

import java.util.List;
import java.util.Scanner;

import model.service.UserService;
import model.service.UserServiceImpl;

public class OrderUI {
    private final ProductController productController;
    private final OrderController  controller;
    private final Scanner scanner = new Scanner(System.in);
    private static  final CartController cartController = new CartController();
    UserServiceImpl userService = new UserServiceImpl();  // Only once
    public OrderUI(OrderController controller,ProductController productController) {
        this.controller = controller;
        this.productController = productController;
    }

    public void start(int userId) {
        while (true) {
            System.out.println("\nOrder Menu");
            System.out.println("1. Place Order");
            System.out.println("2. View All Orders");
            System.out.println("3. View Order Detail");
            System.out.println("4. Cancel Order");
            System.out.println("5.Add Item to Cart");
            System.out.println("6.View Cart");
            System.out.println("7. Back to Main Menu ");
            System.out.print("Choose: ");

            int choice = readInt("Please enter a valid number between 0 and 4: ");
            switch (choice) {
                case 1 -> placeOrder();
                case 2 -> viewAllOrders(userId);
                case 3 -> viewOrderDetail();
                case 4 -> cancelOrder(userId);
                case 5 -> {
                    // ✅ Add Item to Cart - using logged-in user session
                    Users currentUser = userService.loadCurrentSession(); // Automatically get the logged-in user
                    List<ProductResponseDto> products = productController.getAllProducts();
                    view.TableUI<ProductResponseDto> tableUI = new view.TableUI<>();
                    tableUI.getTableDisplay(products);
                    if (currentUser != null) {
                        userId = currentUser.getId(); // ✅ Get user ID from session

                        System.out.print("Enter product UUID: ");
                        String productUUID = scanner.nextLine();

                        System.out.print("Enter Quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer

                        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto(productUUID, quantity);

                        // ✅ Now pass both the cart data and user ID
                        System.out.println(cartController.addItemToCart(cartItemCreateDto, userId));
                    } else {
                        System.out.println("⚠️ No user is currently logged in. Please login first.");
                    }
                }

                case 6 -> {
                    // ✅ View Cart - Automatically using the logged-in user session
                    Users currentUser = userService.loadCurrentSession(); // Use your existing userService instance

                    if (currentUser != null) {
                         userId = currentUser.getId(); // Automatically get the user's ID

                        List<Cart> cartItems = cartController.getCartItemsByUserId(userId);

                        if (!cartItems.isEmpty()) {
                            System.out.println("===== Cart Items =====");
                            cartItems.forEach(cart -> System.out.println(
                                    "Product UUID: " + cart.getProductId() +
                                            ", Quantity: " + cart.getQuantity()));
                        } else {
                            System.out.println("🛒 Your cart is empty.");
                        }

                    } else {
                        System.out.println("⚠️ No user is currently logged in. Please login first.");
                    }
                }

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
        System.out.println("\n========= 🧾 ORDER RECEIPT =========");
        System.out.println("Order Code  : " + order.orderCode());
        System.out.println("Order Date  : " + order.orderDate());
        System.out.println("-------------------------------------");
        System.out.printf("%-20s %5s %10s%n", "Product", "Qty", "Price");
        for (OrderItemDto item : order.items()) {
            System.out.printf("%-20s %5d %10.2f%n",
                    item.productName(), item.quantity(), item.productPrice());
        }
        System.out.println("-------------------------------------");
        System.out.printf("Total Quantity: %d%n", order.totalQuantity());
        System.out.printf("Total Price   : $%.2f%n", order.totalPrice());
        System.out.println("=====================================\n");
    }


    private void viewAllOrders(int userId) {
        List<OrderDTO> orders = controller.getOrdersByUser(userId);
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        orders.forEach(o -> System.out.printf(
                "Order ID: %d, Order Code: %s, Date: %s, Total Items: %d, Total Price: %.2f%n",
                o.id(), o.orderCode(), o.orderDate(), o.totalQuantity(), o.totalPrice()
        ));
    }

    private void viewOrderDetail() {
        int orderId = readPositiveInt("Enter order ID: ");
        OrderDTO order = controller.getOrderDetail(orderId);
        if (order == null) {
            System.out.println("Order not found.");
            return;
        }
        System.out.printf("Order Code: %s, Date: %s, Total Price: %.2f%n",
                order.orderCode(), order.orderDate(), order.totalPrice());

        for (OrderItemDto item : order.items()) {
            System.out.printf(" - %s: %.2f x %d%n",
                    item.productName(), item.productPrice(), item.quantity());
        }
    }

    private void cancelOrder(int userId) {
        List<OrderDTO> orders = controller.getOrdersByUser(userId);
        List<OrderItemDto> orderItems = controller.getAllOrderItemByUserId(userId);
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        TableUI<OrderItemDto> tableUI = new TableUI<>();
        tableUI.getTableDisplay(orderItems);

        int orderId = readPositiveInt("Enter order ID to cancel: ");
        System.out.print("Are you sure you want to cancel this order? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Order cancellation aborted.");
            return;
        }

        boolean canceled = controller.cancelOrder(orderId);
        System.out.println(canceled ? "✅ Order canceled successfully." : "❌ Order not found or could not be canceled.");
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
            number = readInt("Invalid input. Please enter a valid number: ");
            if (number <= 0) {
                System.out.println("Please enter a number greater than 0.");
            }
        } while (number <= 0);
        return number;
    }
}
