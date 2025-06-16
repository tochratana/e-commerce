package view;

import controller.ProductController;
import model.dto.product.ProductCreateDto;
import model.dto.product.ProductResponseDto;
import model.dto.product.UpdateProductDto;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProductUI {
    private final ProductController productController;
    private final Scanner scanner;

    public ProductUI() {
        this.productController = new ProductController();
        this.scanner = new Scanner(System.in);
    }

    // Constructor that accepts external scanner (for integration with main UI)
    public ProductUI(Scanner scanner) {
        this.productController = new ProductController();
        this.scanner = scanner;
    }

    public void start() {
        boolean productManagementRunning = true;

        while (productManagementRunning) {
            try {
                displayProductMenu();
                System.out.print("[+] Insert option: ");
                int choice = getUserChoice();

                switch (choice) {
                    case 1 -> createProduct();
                    case 2 -> viewAllProducts();
                    case 3 -> viewProductById();
                    case 4 -> searchProductsByName();
                    case 5 -> viewProductsByCategory();
                    case 6 -> updateProduct();
                    case 7 -> deleteProduct();
                    case 8 -> insertMillionProducts();
                    case 9 -> readMillionProducts();
                    case 10 -> {
                        System.out.println("Returning to Main Menu...");
                        productManagementRunning = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter a number between 1-10.");
                }

                if (productManagementRunning) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear input buffer
            }
        }
    }

    private void displayProductMenu() {
        System.out.println(completeUITable.showProductMenuUI());
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void createProduct() {
        System.out.println("\n=== Create New Product ===");

        try {
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

            System.out.println("✅ Product created successfully!");
            displayProduct(created);
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid input format. Please enter valid numbers for price, quantity, and category ID.");
        } catch (Exception e) {
            System.err.println("❌ Error creating product: " + e.getMessage());
        }
    }

    private void viewAllProducts() {
        System.out.println("\n=== All Products ===");

        try {
            List<ProductResponseDto> products = productController.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("📦 No products found.");
                return;
            }

            // Check if TableUI is available, otherwise use simple display
            try {
                view.TableUI<ProductResponseDto> tableUI = new view.TableUI<>();
                tableUI.getTableDisplay(products);
            } catch (Exception e) {
                // Fallback to simple display if TableUI is not available
                System.out.printf("Found %d products:%n", products.size());
                products.forEach(this::displayProduct);
            }
        } catch (Exception e) {
            System.err.println("❌ Error retrieving products: " + e.getMessage());
        }
    }

    private void viewProductById() {
        System.out.println("\n=== View Product by ID ===");

        try {
            System.out.print("Enter product UUID: ");
            String uuid = scanner.nextLine();

            Optional<ProductResponseDto> product = productController.getProductById(uuid);

            if (product.isPresent()) {
                displayProduct(product.get());
            } else {
                System.out.println("❌ Product not found with UUID: " + uuid);
            }
        } catch (Exception e) {
            System.err.println("❌ Error retrieving product: " + e.getMessage());
        }
    }

    private void searchProductsByName() {
        System.out.println("\n=== Search Products by Name ===");

        try {
            System.out.print("Enter product name (or prefix): ");
            String name = scanner.nextLine();

            List<ProductResponseDto> products = productController.searchProducts(name);

            if (products.isEmpty()) {
                System.out.println("❌ No products found matching: " + name);
            } else {
                System.out.printf("✅ Found %d products matching '%s':%n", products.size(), name);
                products.forEach(this::displayProduct);
            }
        } catch (Exception e) {
            System.err.println("❌ Error searching products: " + e.getMessage());
        }
    }

    private void viewProductsByCategory() {
        System.out.println("\n=== View Products by Category ===");

        try {
            System.out.print("Enter category ID: ");
            Long categoryId = Long.parseLong(scanner.nextLine());

            List<ProductResponseDto> products = productController.getProductsByCategory(categoryId);

            if (products.isEmpty()) {
                System.out.println("❌ No products found for category ID: " + categoryId);
            } else {
                System.out.printf("✅ Found %d products in category %d:%n", products.size(), categoryId);
                products.forEach(this::displayProduct);
            }
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid category ID format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("❌ Error retrieving products by category: " + e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.println("\n=== Update Product ===");

        try {
            System.out.print("Enter product UUID to update: ");
            String uuid = scanner.nextLine();

            // First check if product exists
            Optional<ProductResponseDto> existingProduct = productController.getProductById(uuid);
            if (existingProduct.isEmpty()) {
                System.out.println("❌ Product not found with UUID: " + uuid);
                return;
            }

            System.out.println("Current product details:");
            displayProduct(existingProduct.get());

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

            System.out.println("✅ Product updated successfully!");
            displayProduct(updated);
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid input format. Please enter valid numbers for price, quantity, and category ID.");
        } catch (Exception e) {
            System.err.println("❌ Error updating product: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        System.out.println("\n=== Delete Product ===");

        try {
            System.out.print("Enter product UUID to delete: ");
            String uuid = scanner.nextLine();

            // First check if product exists
            Optional<ProductResponseDto> existingProduct = productController.getProductById(uuid);
            if (existingProduct.isEmpty()) {
                System.out.println("❌ Product not found with UUID: " + uuid);
                return;
            }

            System.out.println("Product to be deleted:");
            displayProduct(existingProduct.get());

            System.out.print("Are you sure you want to delete this product? (yes/no): ");
            String confirmation = scanner.nextLine();

            if ("yes".equalsIgnoreCase(confirmation) || "y".equalsIgnoreCase(confirmation)) {
                productController.deleteProduct(uuid);
                System.out.println("✅ Product deleted successfully!");
            } else {
                System.out.println("❌ Delete operation cancelled.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error deleting product: " + e.getMessage());
        }
    }

    private void insertMillionProducts() {
        System.out.println("\n=== Performance Test: Insert Million Products ===");
        System.out.print("⚠️  This will insert 1 million products. Continue? (yes/no): ");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation) || "y".equalsIgnoreCase(confirmation)) {
            System.out.println("🚀 Starting bulk insert operation...");
            try {
                productController.insertMillionProducts();
                System.out.println("✅ Bulk insert operation completed!");
            } catch (Exception e) {
                System.err.println("❌ Error during bulk insert: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Bulk insert operation cancelled.");
        }
    }

    private void readMillionProducts() {
        System.out.println("\n=== Performance Test: Read Million Products ===");
        System.out.print("⚠️  This will read all active products. Continue? (yes/no): ");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation) || "y".equalsIgnoreCase(confirmation)) {
            System.out.println("🚀 Starting bulk read operation...");
            try {
                productController.readMillionProducts();
                System.out.println("✅ Bulk read operation completed!");
            } catch (Exception e) {
                System.err.println("❌ Error during bulk read: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Bulk read operation cancelled.");
        }
    }

    private void displayProduct(ProductResponseDto product) {
        System.out.println(completeUITable.displayProduct(product));
//        System.out.println("┌─────────────────────────────────────────────────────────────┐");
//        System.out.printf("│ UUID: %-53s │%n", product.getUuid());
//        System.out.printf("│ Name: %-53s │%n", product.getName());
//        System.out.printf("│ Price: $%-50.2f │%n", product.getPrice());
//        System.out.printf("│ Quantity: %-47d │%n", product.getQuantity());
//        System.out.printf("│ Category: %-47s │%n", product.getCategoryName());
//        System.out.printf("│ Status: %-49s │%n", product.getIsDeleted() ? "Deleted" : "Active");
//        System.out.println("└─────────────────────────────────────────────────────────────┘");
    }
}