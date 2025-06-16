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
import model.entities.Product;
import model.entities.Users;

import java.util.*;

import model.service.ProductService;
import model.service.UserService;
import model.service.UserServiceImpl;

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
            System.out.println("\n==================== Order Menu ====================");
            System.out.println("| 1. Add Item to Cart                              |");
            System.out.println("| 2. View Cart                                     |");
            System.out.println("| 3. Place Order                                   |");
            System.out.println("| 4. View All Orders                               |");
            System.out.println("| 5. View Order Detail                             |");
            System.out.println("| 6. Cancel Order                                  |");
            System.out.println("| 7. Back to Main Menu                             |");
            System.out.println("====================================================");
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
        System.out.printf("Order Code: %s, Date: %s, Total Price: %.2f%n",
                order.orderCode(), order.orderDate(), order.totalPrice());

        // Display order items as table
        List<OrderItemDto> items = order.items();

        if (items == null || items.isEmpty()) {
            System.out.println("No items in this order.");
            return;
        }

        TableUI<OrderItemDto> tableUI = new TableUI<>();
        tableUI.getTableDisplay(items);
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

    public void addItemToCart(int userId) {
        // ✅ Load currently logged-in user
        Users currentUser = userService.loadCurrentSession();
        if (currentUser == null) {
            System.out.println("\n⚠️ No user is currently logged in. Please log in first.");
            return;
        }

        userId = currentUser.getId();

        // ✅ Display products
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
                System.out.println("❌ Invalid UUID format. Please enter a valid UUID.");
            }
        }

        int quantity;
        while (true) {
            System.out.print("→ Enter Quantity: ");
            String quantityInput = scanner.nextLine().trim();
            try {
                quantity = Integer.parseInt(quantityInput);
                if (quantity <= 0) {
                    System.out.println("❌ Quantity must be a positive number.");
                } else {
                    break; // valid quantity, exit loop
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid quantity. Please enter a valid number.");
            }
        }

        // ✅ Create DTO and call controller
        CartItemCreateDto cartItemCreateDto = new CartItemCreateDto(productUUID, quantity);

        // Assuming your cartController.addItemToCart returns a String message
        String result = cartController.addItemToCart(cartItemCreateDto, userId);
        System.out.println("\n✅ " + result);
    }

    public void viewCart(int userId) {
        Users currentUser = userService.loadCurrentSession();

        if (currentUser == null) {
            System.out.println("\n⚠️ No user is currently logged in. Please login first.\n");
            return;
        }

        userId = currentUser.getId();
        List<Cart> cartItems = cartController.getCartItemsByUserId(userId);

        System.out.println("\n================== Your Cart ==================");

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
            number = readInt("Invalid input. Please enter a valid number: ");
            if (number <= 0) {
                System.out.println("Please enter a number greater than 0.");
            }
        } while (number <= 0);
        return number;
    }
}
