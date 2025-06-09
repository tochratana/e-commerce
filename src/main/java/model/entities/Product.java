package model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String uuid;
    private String name;
    private double price;
    private int quantity;
    private Long categoryId;

    public Product(String name, double price, int quantity, Long categoryId) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }
}