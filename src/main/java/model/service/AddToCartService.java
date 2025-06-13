package model.service;

import model.dto.product.AddToCartDto;

import java.sql.SQLException;

public interface AddToCartService {
    void addToCart(model.dto.product.AddToCartDto dto);

    void viewCart() throws SQLException;
    void removeFromCart(String productUuid) throws SQLException;
}