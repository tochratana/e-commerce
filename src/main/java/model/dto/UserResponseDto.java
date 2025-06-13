package model.dto;

public record UserResponseDto(
        String username,
        String email,
        String uuid
) {
}
