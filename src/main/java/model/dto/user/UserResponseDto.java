package model.dto.user;

public record UserResponseDto(
        Integer id,
        String username,
        String email,
        String uuid

) {
}
