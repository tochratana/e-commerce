package model.dto;

public record OrderItemDto(
        String orderCode,
        String code, Integer productId,
        Integer quantity,
        Double price
) {}
