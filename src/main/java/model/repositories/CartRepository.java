package model.repositories;
import model.dto.CartItemCreateDto;
import model.entities.CartItem;

import java.util.List;

public interface CartRepository {
    CartItem addItemToCart(CartItemCreateDto cartItemCreateDto, Integer userId);
    List<CartItem> getCartItemsByUserId(int userId);
    void clearCartByUserId(int userId);
}
