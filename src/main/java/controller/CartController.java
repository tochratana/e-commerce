package controller;

import model.dto.CartItemCreateDto;
import model.entities.Cart;
import model.entities.CartItem;
import model.service.CartItemServiceImpl;

import java.util.List;

public class CartController {
    private final CartItemServiceImpl cartService = new CartItemServiceImpl();

    public CartItem addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId) {
        return cartService.addItemToCart(cartItemCreateDto, userId);
    }

    public List<Cart> getCartItemsByUserId(Integer userId) {
        return cartService.getCartItemsByUserId(userId);
    }
}