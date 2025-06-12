package model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardItem {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
}
