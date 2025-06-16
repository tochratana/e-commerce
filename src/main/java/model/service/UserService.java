package model.service;

import model.dto.user.DeleteUserDto;
import model.dto.user.UpdateUserDto;
import model.dto.user.UserCreateDto;
import model.dto.user.UserResponseDto;
import model.entities.Users;

import java.util.List;

public interface UserService {
    UserResponseDto login(String email, String password);
    List<UserResponseDto> getAllUsers();
    UserResponseDto register(UserCreateDto user);
    Integer deleteUserByUuid(String uuid, DeleteUserDto deleteUserDto);
    UserResponseDto getUserByUuid(String uuid);
    UserResponseDto updateUserByUuid(String uuid, UpdateUserDto updateUserDto);
    void writeSessionToFile(Users user);  // Remove String action parameter
    boolean logout();                     // Remove String uuid parameter
    Users loadCurrentSession();
    void clearCurrentSession();
}