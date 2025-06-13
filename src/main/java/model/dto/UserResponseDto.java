package model.dto;

public record UserResponseDto(
        Integer id,
        String username,
        String email,
        String uuid
) {
}
