package model.dto.order;

import java.time.LocalDate;
import java.util.List;
public record OrderDTO(
        int id,
        String orderCode,
        int totalQuantity,
        double totalPrice,
        LocalDate orderDate,
        List<OrderItemDto> items
) {}