package controller;

import model.dto.CartItemCreateDto;
import model.entities.Cart;
import model.entities.CartItem;
import model.service.CartItemServiceImpl;

import java.util.List;

public class CartController {
    private final CartItemServiceImpl cartService = new CartItemServiceImpl();

    public String addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId) {
        try {
            cartService.addItemToCart(cartItemCreateDto, userId);
            return "Item successfully added to cart.";
        } catch (IllegalArgumentException e) {
            return "❌ Error: " + e.getMessage();
        } catch (Exception e) {
            return "❌ Unexpected error: " + e.getMessage();
        }
    }


    public List<Cart> getCartItemsByUserId(Integer userId) {
        return cartService.getCartItemsByUserId(userId);
    }
}