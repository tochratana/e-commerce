package model.dto;

import lombok.Data;

public record UserCreateDto(
        String username,
        String email,
        String password
) {
}
