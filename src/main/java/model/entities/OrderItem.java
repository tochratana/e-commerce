package model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    private int id;
    private int orderId;
    private UUID productId;
    private String productName;
    private double productPrice;
    private int quantity;

}
