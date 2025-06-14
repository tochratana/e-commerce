package model.dto.order;

public record OrderItemDto(
        int orderId,
        String productName,
        double productPrice,
        int quantity
) {}
