package model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDto {
    private String uuid;
    private String name;
    private double price;
    private int quantity;
    private Long categoryId;
}
