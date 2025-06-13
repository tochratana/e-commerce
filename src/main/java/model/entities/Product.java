package model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private UUID uuid;  // Changed from String to UUID
    private String name;
    private double price;
    private int quantity;
    private Long categoryId;
    private Boolean isDeleted;

    // Additional constructor for convenience
    public Product(String name, double price, int quantity, Long categoryId) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.isDeleted = false;
    }


}