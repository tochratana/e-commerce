package mapper;

import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import model.entities.Users;
import utils.PasswordHash;

import java.util.Random;
import java.util.UUID;

public class UserMapper {
    public static Users mapFromUserCreateDto(UserCreateDto user) {
        String hashedPassword = PasswordHash.hashPassword(user.password());
        if (user == null || user.username() == null || user.email() == null || user.password() == null) {
            throw new IllegalArgumentException("UserCreateDto fields must not be null");
        }
        return new Users(new Random().nextInt(999999999),
                user.username(),
                user.email(),
                hashedPassword,
                false,
                UUID.randomUUID().toString(),
                false);
    }
    public static UserResponseDto mapToUserResponseDto(Users users) {
        return new UserResponseDto(users.getUsername(), users.getEmail(),users.getUuid());
    }
}
