package model.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private String uuid;
    private String name;
    private double price;
    private int quantity;
    private String categoryName;
    private Boolean isDeleted;
}