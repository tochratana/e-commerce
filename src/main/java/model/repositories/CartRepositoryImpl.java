package model.repositories;

import model.dto.CartItemCreateDto;
import model.entities.Cart;
import model.entities.CartItem;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartRepositoryImpl {

    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    public CartItem addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId) {
        String SQL = """
            INSERT INTO cart_items(user_id, product_id, quantity)
            VALUES (?, ?, ?)
            RETURNING id, user_id, product_id, quantity
            """;

        try (Connection con = databaseConfig.getDatabaseConnection()) {  // Use DatabaseConfig here
            PreparedStatement pre = con.prepareStatement(SQL);
            pre.setInt(1, userId);
            pre.setObject(2, UUID.fromString(cartItemCreateDto.productUuid()));
            pre.setInt(3, cartItemCreateDto.quantity());

            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setId(rs.getInt("id"));
                cartItem.setUserId(rs.getInt("user_id"));
                cartItem.setProductId(rs.getObject("product_id", UUID.class));
                cartItem.setQuantity(rs.getInt("quantity"));
                return cartItem;
            }
        } catch (Exception exception) {
            System.out.println("Error during adding item to cart... " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }


    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = """
        SELECT ci.id, ci.user_id, ci.product_id, p.name, p.price, ci.quantity
        FROM cart_items ci
        JOIN products p ON ci.product_id = p.uuid
        WHERE ci.user_id = ?;
    """;

        try (Connection conn = databaseConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("user_id"));
                item.setProductId(rs.getObject("product_id", UUID.class));
                item.setProductName(rs.getString("name"));
                item.setProductPrice(rs.getDouble("price"));
                item.setQuantity(rs.getInt("quantity"));

                cartItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // or use a logger
        }

        return cartItems;
    }
    public void clearCartByUserId(int userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";

        try (Connection conn = databaseConfig.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally throw a custom exception or handle error properly
        }
    }

}
