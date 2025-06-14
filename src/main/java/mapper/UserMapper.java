package mapper;

import model.dto.user.UserCreateDto;
import model.dto.user.UserResponseDto;
import model.entities.Users;
import utils.PasswordHash; // ✅ Import your PasswordHash utility

import java.util.Random;
import java.util.UUID;

public class UserMapper {
    public static Users mapFromUserCreateDto(UserCreateDto dto) {
        if (dto == null || dto.username() == null || dto.email() == null || dto.password() == null) {
            throw new IllegalArgumentException("UserCreateDto fields must not be null");
        }

        // ✅ Hash the password before storing
        String hashedPassword = PasswordHash.hashPassword(dto.password());

        return new Users(new Random().nextInt(999999999),
                dto.username(),
                dto.email(),
                hashedPassword, // ✅ Store hashed password
                false,
                UUID.randomUUID().toString(),
                false);
    }

    public static UserResponseDto mapToUserResponseDto(Users users) {
        return new UserResponseDto(users.getId(), users.getUsername(), users.getEmail(), users.getUuid());
    }
}