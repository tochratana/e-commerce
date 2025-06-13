package model.service;

import model.dto.DeleteUserDto;
import model.dto.UpdateUserDto;
import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import model.entities.Users;

import java.util.List;

public interface UserService {
    UserResponseDto login(String email, String password);
    List<UserResponseDto> getAllUsers();
    UserResponseDto register(UserCreateDto user);
    Integer deleteUserByUuid(String uuid, DeleteUserDto deleteUserDto);
    UserResponseDto getUserByUuid(String uuid);
    UserResponseDto updateUserByUuid(String uuid, UpdateUserDto updateUserDto);
    void writeSessionToFile(Users user,String action);
    boolean logout(String uuid);
    Users loadCurrentSession();
    void clearCurrentSession();
}
