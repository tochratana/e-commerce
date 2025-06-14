package controller;

import model.dto.user.DeleteUserDto;
import model.dto.user.UpdateUserDto;
import model.dto.user.UserCreateDto;
import model.dto.user.UserResponseDto;
import model.entities.Users;
import model.service.UserServiceImpl;

import java.util.List;

public class UserController {
    private final UserServiceImpl userService = new UserServiceImpl();

    // ✅ Store the logged-in user
    private UserResponseDto loggedInUser;

    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    public UserResponseDto register(UserCreateDto userCreateDto) {
        return userService.register(userCreateDto);
    }

    public UserResponseDto login(String email, String password) {
        this.loggedInUser = userService.login(email, password); // ✅ Save logged-in user
        return this.loggedInUser;
    }

    public UserResponseDto getUserByUuid(String uuid) {
        return userService.getUserByUuid(uuid);
    }

    public Integer deleteUserByUuid(String uuid, DeleteUserDto deleteUserDto) {
        return userService.deleteUserByUuid(uuid, deleteUserDto);
    }

    public UserResponseDto updateUserByUuid(String uuid, UpdateUserDto updateUserDto) {
        return userService.updateUserByUuid(uuid, updateUserDto);
    }

    public boolean callUser() {
        Users currentUser = userService.loadCurrentSession();
        boolean isloggedin = false;
        if (currentUser != null) {
            System.out.println("Welcome back, " + currentUser.getUsername());
            isloggedin = true;
        } else {
            System.out.println("No active session found. Please log in.");
            isloggedin = false;
        }
        return isloggedin;
    }

    // ✅ Getter to access logged-in user from other classes
    public UserResponseDto getLoggedInUser() {
        return this.loggedInUser;
    }
}
