package model.repositories;

import model.dto.CartItemCreateDto;
import model.entities.Cart;
import model.entities.CartItem;

import java.util.List;

import java.util.List;

public interface CartItemRepository {
    CartItem addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId);
    List<Cart> getCartItemsByUserId(Integer userId);
}

