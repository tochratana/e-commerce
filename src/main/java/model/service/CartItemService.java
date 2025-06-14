package model.service;

import model.dto.CartItemCreateDto;
import model.entities.Cart;
import model.entities.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId);
    List<Cart> getCartItemsByUserId(Integer userId);

}
