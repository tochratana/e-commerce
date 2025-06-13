package model.service;
import model.dto.product.AddToCartDto;
import model.entities.CartItem;
import model.entities.Product;
import model.repositories.CartItemRepository;
import model.repositories.ProductRepository;

import java.sql.SQLException;

public class AddToCartServiceImpl implements AddToCartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartRepo;

    public AddToCartServiceImpl(ProductRepository productRepository, CartItemRepository cartRepo) {
        this.productRepository = productRepository;
        this.cartRepo = cartRepo;
    }

    @Override
    public void addToCart(AddToCartDto dto) {
        try {
            Product product = productRepository.getProductByUUID(dto.getProductUuid());

            if (product == null) {
                System.out.println("❌ Product not found.");
                return;
            }

            if (product.getQuantity() < dto.getQuantity()) {
                System.out.println("⚠ Not enough stock.");
                return;
            }
            CartItem item = new CartItem(
                    product.getUuid().toString(),
                    product.getName(),
                    product.getPrice(),
                    dto.getQuantity()
            );
            cartRepo.add(item);
            System.out.println("✅ Added to cart: " + product.getName());

        } catch (Exception e) {
            System.out.println("❌ DB Error: " + e.getMessage());
        }
    }

    @Override
    public void viewCart() throws SQLException {
        if (cartRepo.isEmpty()) {
            System.out.println("🛒 Cart is empty.");
            return;
        }

        System.out.println("🧾 Your Cart:");
        for (CartItem item : cartRepo.getAll()) {
            System.out.printf("- %s x%d = $%.2f\n",
                    item.getProductName(), item.getQuantity(), item.getTotalPrice());
        }
        System.out.println("Total: $" + cartRepo.getTotal());
    }

    @Override
    public void removeFromCart(String productUuid) throws SQLException {
        cartRepo.remove(productUuid);
        System.out.println("🗑️ Removed item from cart.");
    }
}
