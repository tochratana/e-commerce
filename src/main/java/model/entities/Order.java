package model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private int id;
    private int userId;
    private String orderCode;
    private int totalQuantity;
    private double totalPrice;
    private LocalDate orderDate;

    private List<OrderItem> items;
}
