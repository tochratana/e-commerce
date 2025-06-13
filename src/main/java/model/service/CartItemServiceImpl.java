package model.service;

import model.repositories.CartItemRepositoryImpl;
import model.repositories.CartRepositoryImpl;
import model.dto.CartItemCreateDto;
import model.entities.CartItem;
import model.entities.Cart;
import java.util.List;

public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepositoryImpl  cartItemRepository = new CartItemRepositoryImpl();

    public CartItem addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId) {
        return cartItemRepository.addItemToCart(cartItemCreateDto, userId);
    }

    public List<Cart> getCartItemsByUserId(Integer userId) {
        return cartItemRepository.getCartItemsByUserId(userId);
    }
} 
