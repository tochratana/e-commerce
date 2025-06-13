package controller;

import model.dto.DeleteUserDto;
import model.dto.UpdateUserDto;
import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import model.entities.Users;
import model.service.UserServiceImpl;

import java.util.List;

public class UserController {
    private UserServiceImpl userService = new UserServiceImpl();
    public List<UserResponseDto> getAllUsers(){
        return userService.getAllUsers();
    }
    public UserResponseDto register(UserCreateDto userCreateDto){
        return userService.register(userCreateDto);
    }
    public UserResponseDto login(String email, String password){return userService.login(email,password);}
    public UserResponseDto getUserByUuid(String uuid){
        return userService.getUserByUuid(uuid);
    }
    public Integer deleteUserByUuid(String uuid, DeleteUserDto deleteUserDto){
        return userService.deleteUserByUuid(uuid, deleteUserDto);
    }
    public UserResponseDto updateUserByUuid(String uuid, UpdateUserDto updateUserDto){
        return userService.updateUserByUuid(uuid, updateUserDto);
    }
    public boolean logout(){
        return userService.logout();
    }
    public boolean callUser(){
        UserServiceImpl userService = new UserServiceImpl();
        Users currentUser = userService.loadCurrentSession();
        boolean isloggedin = false;
        if (currentUser != null) {
            System.out.println("Welcome " + currentUser.getUsername());
            isloggedin = true;
        } else {
            System.out.println("No active session found. Please log in.");
            isloggedin = false;
        }
        return isloggedin;
    }
}
