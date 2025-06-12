package debug;

import model.repositories.ProductRepository;
import model.entities.Product;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class ProductDebugHelper {
    private final ProductRepository productRepository = new ProductRepository();
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public void debugProductLookup(String uuidString) {
        System.out.println("=== DEBUG: Product Lookup ===");
        System.out.println("Input UUID: " + uuidString);

        // Validate UUID format
        try {
            UUID.fromString(uuidString);
            System.out.println("✓ UUID format is valid");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Invalid UUID format: " + e.getMessage());
            return;
        }

        // Check database connection
        try (Connection conn = dbConfig.getDatabaseConnection()) {
            if (conn == null) {
                System.out.println("✗ Database connection failed");
                return;
            }
            System.out.println("✓ Database connection successful");

            // Direct SQL query to check if product exists
            checkProductExistence(conn, uuidString);

            // Test repository method
            testRepositoryMethod(uuidString);

        } catch (SQLException e) {
            System.out.println("✗ Database error: " + e.getMessage());
        }
    }

    private void checkProductExistence(Connection conn, String uuidString) {
        System.out.println("\n--- Direct Database Query ---");

        // Query 1: Check if UUID exists at all
        String sql1 = "SELECT uuid, name, is_deleted FROM products WHERE uuid = ?::uuid";
        try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
            stmt.setString(1, uuidString);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✓ Product found in database:");
                System.out.println("  UUID: " + rs.getString("uuid"));
                System.out.println("  Name: " + rs.getString("name"));
                System.out.println("  Is Deleted: " + rs.getBoolean("is_deleted"));
            } else {
                System.out.println("✗ Product not found in database");
            }
        } catch (SQLException e) {
            System.out.println("✗ Direct query failed: " + e.getMessage());
        }

        // Query 2: Check with is_deleted filter
        String sql2 = "SELECT uuid, name, is_deleted FROM products WHERE uuid = ?::uuid AND is_deleted = false";
        try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
            stmt.setString(1, uuidString);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("✓ Active product found:");
                System.out.println("  UUID: " + rs.getString("uuid"));
                System.out.println("  Name: " + rs.getString("name"));
            } else {
                System.out.println("✗ No active product found (may be deleted)");
            }
        } catch (SQLException e) {
            System.out.println("✗ Filtered query failed: " + e.getMessage());
        }
    }

    private void testRepositoryMethod(String uuidString) {
        System.out.println("\n--- Repository Method Test ---");
        try {
            Optional<Product> product = productRepository.findById(uuidString);
            if (product.isPresent()) {
                System.out.println("✓ Repository method found product:");
                System.out.println("  UUID: " + product.get().getUuid());
                System.out.println("  Name: " + product.get().getName());
            } else {
                System.out.println("✗ Repository method returned empty result");
            }
        } catch (Exception e) {
            System.out.println("✗ Repository method failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void listAllProducts() {
        System.out.println("\n=== All Products in Database ===");
        String sql = "SELECT uuid, name, is_deleted FROM products ORDER BY name LIMIT 10";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("%d. UUID: %s, Name: %s, Deleted: %s%n",
                        count,
                        rs.getString("uuid"),
                        rs.getString("name"),
                        rs.getBoolean("is_deleted"));
            }

            if (count == 0) {
                System.out.println("No products found in database");
            }

        } catch (SQLException e) {
            System.out.println("✗ Failed to list products: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ProductDebugHelper helper = new ProductDebugHelper();

        // List some products first
        helper.listAllProducts();

        // Test with your UUID
        if (args.length > 0) {
            helper.debugProductLookup(args[0]);
        } else {
            System.out.println("\nTo debug a specific UUID, run:");
            System.out.println("java debug.ProductDebugHelper <UUID>");
        }
    }
}
