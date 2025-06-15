package model.repositories;

import model.entities.Product;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepository implements Repository<Product, String> {
    //private final DatabaseConfig dbConfig = new DatabaseConfig();

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO products (uuid, name, price, quantity, category_id, is_deleted) VALUES (?::uuid, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, product.getUuid().toString()); // Convert UUID to String
                stmt.setString(2, product.getName());
                stmt.setDouble(3, product.getPrice());
                stmt.setInt(4, product.getQuantity());
                stmt.setLong(5, product.getCategoryId());
                stmt.setBoolean(6, product.getIsDeleted() != null ? product.getIsDeleted() : false);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Failed to save product - no rows affected");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in save: " + e.getMessage());
            throw new RuntimeException("Error saving product", e);
        }

        return product;
    }

    @Override
    public Optional<Product> findById(String uuid) {
        String sql = "SELECT * FROM products WHERE uuid = ?::uuid AND is_deleted = false";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid); // Set as String, let PostgreSQL cast it

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToProduct(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in findById: " + e.getMessage());
            System.err.println("UUID parameter: " + uuid);
            throw new RuntimeException("Error finding product by uuid", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE is_deleted = false ORDER BY name";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in findAll: " + e.getMessage());
            throw new RuntimeException("Error finding all products", e);
        }
        return products;
    }

    @Override
    public void deleteById(String uuid) {
        // First check if product exists
        if (findById(uuid).isEmpty()) {
            throw new IllegalArgumentException("Product not found with UUID: " + uuid);
        }

        // Soft delete: set is_deleted = true
        String sql = "UPDATE products SET is_deleted = true, updated_at = CURRENT_TIMESTAMP WHERE uuid = ?::uuid";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Failed to delete product - no rows affected");
                }

                System.out.println("Successfully soft-deleted product with UUID: " + uuid);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in deleteById: " + e.getMessage());
            throw new RuntimeException("Error soft deleting product", e);
        }
    }

    @Override
    public Product update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ?, category_id = ?, updated_at = CURRENT_TIMESTAMP WHERE uuid = ?::uuid AND is_deleted = false";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, product.getName());
                stmt.setDouble(2, product.getPrice());
                stmt.setInt(3, product.getQuantity());
                stmt.setLong(4, product.getCategoryId());
                stmt.setString(5, product.getUuid().toString());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Failed to update product - product not found or already deleted");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in update: " + e.getMessage());
            throw new RuntimeException("Error updating product", e);
        }
        return product;
    }

    public List<Product> findByCategory(Long categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ? AND is_deleted = false ORDER BY name";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, categoryId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapResultSetToProduct(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in findByCategory: " + e.getMessage());
            throw new RuntimeException("Error finding products by category", e);
        }
        return products;
    }

    public List<Product> findByNameStartingWith(String prefix) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?) AND is_deleted = false ORDER BY name";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, prefix + "%");

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapResultSetToProduct(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in findByNameStartingWith: " + e.getMessage());
            throw new RuntimeException("Error searching products by name", e);
        }
        return products;
    }

    public void batchInsert(List<Product> products) {
        String sql = "INSERT INTO products (uuid, name, price, quantity, category_id, is_deleted) VALUES (?::uuid, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);

                for (Product product : products) {
                    stmt.setString(1, product.getUuid().toString());
                    stmt.setString(2, product.getName());
                    stmt.setDouble(3, product.getPrice());
                    stmt.setInt(4, product.getQuantity());
                    stmt.setLong(5, product.getCategoryId());
                    stmt.setBoolean(6, product.getIsDeleted() != null ? product.getIsDeleted() : false);
                    stmt.addBatch();
                }

                stmt.executeBatch();
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in batchInsert: " + e.getMessage());
            throw new RuntimeException("Error batch inserting products", e);
        }
    }

    public void restoreById(String uuid) {
        String sql = "UPDATE products SET is_deleted = false, updated_at = CURRENT_TIMESTAMP WHERE uuid = ?::uuid";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Failed to restore product - product not found");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in restoreById: " + e.getMessage());
            throw new RuntimeException("Error restoring product", e);
        }
    }

    public List<Product> findAllIncludingDeleted() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY name";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in findAllIncludingDeleted: " + e.getMessage());
            throw new RuntimeException("Error finding all products including deleted", e);
        }
        return products;
    }

    public void hardDeleteById(String uuid) {
        String sql = "DELETE FROM products WHERE uuid = ?::uuid";
        try (Connection conn = DatabaseConfig.getDatabaseConnection()) {
            if (conn == null) {
                throw new RuntimeException("Failed to establish a database connection.");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, uuid);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Failed to permanently delete product - product not found");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in hardDeleteById: " + e.getMessage());
            throw new RuntimeException("Error permanently deleting product", e);
        }
    }

    // Helper method to validate UUID format
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                UUID.fromString(rs.getString("uuid")),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getLong("category_id"),
                rs.getBoolean("is_deleted")
        );
    }
}