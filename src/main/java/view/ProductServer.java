package view;

import controller.ProductController;
import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;
import view.completeUITable;
import static view.Color.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProductServer {
    private final ProductController productController;
    private final Scanner scanner;


    public ProductServer() {
        this.productController = new ProductController();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println(GREEN + "=== Product Management Server ===" + RESET);
        System.out.println("Server started successfully!");

        while (true) {
            System.out.println(completeUITable.showProductMenuUI());
//            displayMenu();
            System.out.print("[+] Enter your choice (1-10): ");
            int choice = getUserChoice();

            try {
                handleMenuChoice(choice);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Product Management Menu ===");
        System.out.println("1. Create Product");
        System.out.println("2. View All Products");
        System.out.println("3. View Product by ID");
        System.out.println("4. Search Products by Name");
        System.out.println("5. View Products by Category");
        System.out.println("6. Update Product");
        System.out.println("7. Delete Product");
        System.out.println("8. Insert Million Products (Performance Test)");
        System.out.println("9. Read Million Products (Performance Test)");
        System.out.println("10. Exit");
        System.out.print("Enter your choice (1-10): ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    //todo (part: back to menu not work)
    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                createProduct();
                break;
            case 2:
                viewAllProducts();
                break;
            case 3:
                viewProductById();
                break;
            case 4:
                searchProductsByName();
                break;
            case 5:
                viewProductsByCategory();
                break;
            case 6:
                updateProduct();
                break;
            case 7:
                deleteProduct();
                break;
            case 8:
                insertMillionProducts();
                break;
            case 9:
                readMillionProducts();
                break;
            case 10:
                exitApplication();
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1-10.");
        }
    }

    private void createProduct() {
        System.out.println("\n=== Create New Product ===");

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter product quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter category ID: ");
        Long categoryId = Long.parseLong(scanner.nextLine());

        ProductCreateDto createDto = new ProductCreateDto(name, price, quantity, categoryId);
        ProductResponseDto created = productController.createProduct(createDto);

        System.out.println("Product created successfully!");
        displayProduct(created);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void viewAllProducts() {
        TableUI<ProductResponseDto> tableUI = new TableUI<>();
        System.out.println("\n=== All Products ===");

        List<ProductResponseDto> products = productController.getAllProducts();
        tableUI.getTableDisplay(products);


        if (products.isEmpty()) {
            System.out.println("No products found.");
        }
//        else {
//            System.out.printf("Found %d products:%n", products.size());
//            products.forEach(this::displayProduct);
//        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void viewProductById() {
        System.out.println("\n=== View Product by ID ===");
        System.out.print("Enter product UUID: ");
        String uuid = scanner.nextLine();

        Optional<ProductResponseDto> product = productController.getProductById(uuid);

        if (product.isPresent()) {
            displayProduct(product.get());
        } else {
            System.out.println("Product not found.");
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void searchProductsByName() {
        System.out.println("\n=== Search Products by Name ===");
        System.out.print("Enter product name (or prefix): ");
        String name = scanner.nextLine();

        List<ProductResponseDto> products = productController.searchProducts(name);

        if (products.isEmpty()) {
            System.out.println("No products found matching: " + name);
        } else {
            System.out.printf("Found %d products matching '%s':%n", products.size(), name);
            products.forEach(this::displayProduct);
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void viewProductsByCategory() {
        System.out.println("\n=== View Products by Category ===");
        System.out.print("Enter category ID: ");
        Long categoryId = Long.parseLong(scanner.nextLine());

        List<ProductResponseDto> products = productController.getProductsByCategory(categoryId);

        if (products.isEmpty()) {
            System.out.println("No products found for category ID: " + categoryId);
        } else {
            System.out.printf("Found %d products in category %d:%n", products.size(), categoryId);
            products.forEach(this::displayProduct);
        }
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void updateProduct() {
        System.out.println("\n=== Update Product ===");

        System.out.print("Enter product UUID to update: ");
        String uuid = scanner.nextLine();

        System.out.print("Enter new product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new product price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter new product quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new category ID: ");
        Long categoryId = Long.parseLong(scanner.nextLine());

        UpdateProductDto updateDto = new UpdateProductDto(uuid, name, price, quantity, categoryId);
        ProductResponseDto updated = productController.updateProduct(updateDto);

        System.out.println("Product updated successfully!");
        displayProduct(updated);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void deleteProduct() {
        System.out.println("\n=== Delete Product ===");
        System.out.print("Enter product UUID to delete: ");
        String uuid = scanner.nextLine();

        System.out.print("Are you sure you want to delete this product? (y/N): ");
        String confirmation = scanner.nextLine();

        if ("y".equalsIgnoreCase(confirmation) || "yes".equalsIgnoreCase(confirmation)) {
            productController.deleteProduct(uuid);
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Delete operation cancelled.");
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void insertMillionProducts() {
        System.out.println("\n=== Performance Test: Insert Million Products ===");
        System.out.print("This will insert 10 million products. Continue? (y/N): ");
        String confirmation = scanner.nextLine();

        if ("y".equalsIgnoreCase(confirmation) || "yes".equalsIgnoreCase(confirmation)) {
            System.out.println("Starting bulk insert operation...");
            productController.insertMillionProducts();
        } else {
            System.out.println("Bulk insert operation cancelled.");
        }
    }

    private void readMillionProducts() {
        System.out.println("\n=== Performance Test: Read Million Products ===");
        System.out.print("This will read all active products. Continue? (y/N): ");
        String confirmation = scanner.nextLine();

        if ("y".equalsIgnoreCase(confirmation) || "yes".equalsIgnoreCase(confirmation)) {
            System.out.println("Starting bulk read operation...");
            productController.readMillionProducts();
        } else {
            System.out.println("Bulk read operation cancelled.");
        }
    }

    private void displayProduct(ProductResponseDto product) {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.printf("│ UUID: %-53s │%n", product.getUuid());
        System.out.printf("│ Name: %-53s │%n", product.getName());
        System.out.printf("│ Price: $%-50.2f │%n", product.getPrice());
        System.out.printf("│ Quantity: %-47d │%n", product.getQuantity());
        System.out.printf("│ Category: %-47s │%n", product.getCategoryName());
        System.out.printf("│ Deleted: %-48s │%n", product.getIsDeleted());
        System.out.println("└─────────────────────────────────────────────────────────────┘");
    }

    private void exitApplication() {
        System.out.println("\n=== Shutting Down ===");
        System.out.println("Thank you for using Product Management Server!");
        scanner.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            ProductServer server = new ProductServer();
            server.start();
        } catch (Exception e) {
            System.err.println("Failed to start Product Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}