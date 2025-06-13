package mapper;

import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import model.entities.Users;

import java.util.Random;
import java.util.UUID;

public class UserMapper {
    public static Users mapFromUserCreateDto(UserCreateDto dto) {
        if (dto == null || dto.username() == null || dto.email() == null || dto.password() == null) {
            throw new IllegalArgumentException("UserCreateDto fields must not be null");
        }
        return new Users(new Random().nextInt(999999999),
                dto.username(),
                dto.email(),
                dto.password(),
                false,
                UUID.randomUUID().toString(),
                false);
    }
    public static UserResponseDto mapToUserResponseDto(Users users) {
        return new UserResponseDto(users.getId(), users.getUsername(), users.getEmail(), users.getUuid());
    }

}
