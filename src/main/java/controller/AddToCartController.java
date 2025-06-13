package controller;

import model.dto.product.AddToCartDto;
import model.service.AddToCartService;

import java.sql.SQLException;
import java.util.Scanner;

public class AddToCartController {
    private final AddToCartService cartService;
    private final Scanner scanner;

    public AddToCartController(AddToCartService cartService) {
        this.cartService = cartService;
        this.scanner = new Scanner(System.in);
    }

    public void addToCartByInput(String uuid, int qty) {
        cartService.addToCart(new AddToCartDto(uuid, qty));
    }

    public void viewCart() throws SQLException {
        cartService.viewCart();
    }

    public void removeFromCart(String uuid) throws SQLException {
        cartService.removeFromCart(uuid);
    }}

