package model.repositories;

import model.entities.Product;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements Repository<Product, String> {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO products (uuid, name, price, quantity, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getUuid());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            stmt.setLong(5, product.getCategoryId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }
        return product;
    }

    @Override
    public Optional<Product> findById(String uuid) {
        String sql = "SELECT * FROM products WHERE uuid = ?";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by uuid", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = dbConfig.getDatabaseConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all products", e);
        }
        return products;
    }

    @Override
    public void deleteById(String uuid) {
        String sql = "DELETE FROM products WHERE uuid = ?";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    @Override
    public Product update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ?, category_id = ? WHERE uuid = ?";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setLong(4, product.getCategoryId());
            stmt.setString(5, product.getUuid());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
        return product;
    }

    public List<Product> findByCategory(Long categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding products by category", e);
        }
        return products;
    }

    public List<Product> findByNameStartingWith(String prefix) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?)";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, prefix + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching products by name", e);
        }
        return products;
    }

    public void batchInsert(List<Product> products) {
        String sql = "INSERT INTO products (uuid, name, price, quantity, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Product product : products) {
                stmt.setString(1, product.getUuid());
                stmt.setString(2, product.getName());
                stmt.setDouble(3, product.getPrice());
                stmt.setInt(4, product.getQuantity());
                stmt.setLong(5, product.getCategoryId());
                stmt.addBatch();
            }

            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException("Error batch inserting products", e);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("uuid"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getLong("category_id")
        );
    }
}
