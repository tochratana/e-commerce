package model.dto;

public record CartItemCreateDto(
        String productUuid,
        Integer quantity
) {
}