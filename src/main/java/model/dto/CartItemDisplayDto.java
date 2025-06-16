package model.dto;

public record CartItemDisplayDto(
        String productName,
        String productUuid,
        Integer quantity
) {}
