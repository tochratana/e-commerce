package model.dto.user;

public record UserCreateDto(
        String username,
        String email,
        String password
) {
}
