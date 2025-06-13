package model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String itemCode;
    private String orderCode;
    private Integer productId;
    private Integer quantity;
    private Double price;
}
