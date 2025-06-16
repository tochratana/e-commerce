package model.repositories;

import model.dto.CartItemCreateDto;
import model.entities.Cart;
import model.entities.CartItem;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartItemRepositoryImpl implements CartItemRepository {
    private final DatabaseConfig databaseConfig = new DatabaseConfig();

    public CartItem addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId) {
        String insertSQL = """
        INSERT INTO cart_items(user_id, product_id, quantity)
        VALUES (?, ?, ?)
        RETURNING id
        """;
        String selectSQL = """
        SELECT ci.id, ci.user_id, ci.product_id, ci.quantity, p.name AS product_name, p.price AS product_price
        FROM cart_items ci
        JOIN products p ON ci.product_id = p.uuid
        WHERE ci.id = ?
        """;

        try (Connection con = databaseConfig.getDatabaseConnection()) {
            // Step 1: Insert and get the new cart_item ID
            int cartItemId = -1;
            try (PreparedStatement insertStmt = con.prepareStatement(insertSQL)) {
                insertStmt.setInt(1, userId);
                insertStmt.setObject(2, UUID.fromString(cartItemCreateDto.productUuid()));
                insertStmt.setInt(3, cartItemCreateDto.quantity());

                ResultSet rs = insertStmt.executeQuery();
                if (rs.next()) {
                    cartItemId = rs.getInt("id");
                }
            }
// Step 2: Select the cart item with product info
            if (cartItemId != -1) {
                try (PreparedStatement selectStmt = con.prepareStatement(selectSQL)) {
                    selectStmt.setInt(1, cartItemId);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        CartItem item = new CartItem();
                        item.setId(rs.getInt("id"));
                        item.setUserId(rs.getInt("user_id"));
                        item.setProductId(rs.getObject("product_id", UUID.class));
                        item.setQuantity(rs.getInt("quantity"));
                        item.setProductName(rs.getString("product_name"));
                        item.setProductPrice(rs.getDouble("product_price"));
                        System.out.println("✅ Product: " + rs.getString("product_name") + ", Price: " + rs.getDouble("product_price"));
                        return item;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error during adding item to cart: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    public List<Cart> getCartItemsByUserId(Integer userId) {
        String SQL = """
                SELECT c.id, c.user_id, c.product_id, c.quantity
                FROM cart_items c
                WHERE c.user_id = ?
                """;

        try (Connection con = databaseConfig.getDatabaseConnection()){
            PreparedStatement pre = con.prepareStatement(SQL);
            pre.setInt(1, userId);
            ResultSet rs = pre.executeQuery();
            List<Cart> cartItems = new ArrayList<>();
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setId(rs.getInt("id"));
                cart.setUserId(rs.getInt("user_id"));
                cart.setProductId(rs.getString("product_id"));
                cart.setQuantity(rs.getInt("quantity"));
                cartItems.add(cart);
            }
            return cartItems;
        } catch (Exception exception) {
            System.out.println("Error during fetching cart items..." + exception.getMessage());
        }
        return new ArrayList<>();
    }
}