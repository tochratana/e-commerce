package model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Integer id;
    private Integer userId;
    private UUID productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
}