package model.dto;

import java.sql.Timestamp;
import java.util.List;

public record OrderDto(
        String orderCode,
        String userId,
        Timestamp orderDate,
        double totalPrice,
        List<OrderItemDto> items
) {}
