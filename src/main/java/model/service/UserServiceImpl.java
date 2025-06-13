package model.service;

import mapper.UserMapper;
import model.dto.DeleteUserDto;
import model.dto.UpdateUserDto;
import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import model.entities.Users;
import model.repositories.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class UserServiceImpl implements UserService {
    private static final UserRepository userRepository = new UserRepository();
    @Override
    public UserResponseDto login(String email, String password) {
        Users user = userRepository.findByUserEmail(email);
        if (user == null || user.is_deleted()) {
            System.out.println("User not found or deleted.");
            return null;
        }
        if (!user.getPassword().equals(password)&&!user.getEmail().equals(email)) {
            System.out.println("Incorrect email or password.");
            return null;
        }
        user.set_logged_in(true);
        userRepository.updateIsLoggedInStatus(user.getUuid(), true);
        writeSessionToFile(user,"Logged in");
        return UserMapper.mapToUserResponseDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserResponseDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            users.add(new UserResponseDto(user.getUsername(), user.getEmail(), user.getUuid()));
        });
        return users;
    }

    @Override
    public UserResponseDto register(UserCreateDto user) {
        Users user1
                = UserMapper.mapFromUserCreateDto(user);
        return UserMapper.mapToUserResponseDto(userRepository.save(user1));
    }

    @Override
    public Integer deleteUserByUuid(String uuid, DeleteUserDto deleteUserDto) {
        try {
            Users deletedUser = userRepository.deleteUser(uuid,deleteUserDto);
            if (deletedUser != null) {
                UserMapper.mapToUserResponseDto(deletedUser);
                return 1;
            }
        } catch (NoSuchElementException e) {
            System.out.println("User not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during deletion: " + e.getMessage());
        }
        return 0;
    }


    @Override
    public UserResponseDto getUserByUuid(String uuid) {
        try{
            return UserMapper.mapToUserResponseDto(
                    userRepository.findByUserUuid(uuid)
            );
        }catch (NoSuchElementException exception){
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public UserResponseDto updateUserByUuid(String uuid, UpdateUserDto updateUserDto) {
        try {
            Users updatedUser = userRepository.updateUser(uuid,updateUserDto);
            return UserMapper.mapToUserResponseDto(updatedUser);
        } catch (NoSuchElementException exception) {
            System.out.println("User not found: " + exception.getMessage());
        } catch (Exception e) {
            System.out.println("Update failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void writeSessionToFile(Users user,String action) {
        try (PrintWriter out = new PrintWriter("current_session.txt")) {
            out.println(user.getUuid());
        } catch (IOException e) {
            System.out.println("Error writing session: " + e.getMessage());
        }
    }

    @Override
    public boolean logout(String uuid) {
        Users user = userRepository.findByUserUuid(uuid);
        if (user == null || !user.is_deleted()) {
            return false;
        }

        userRepository.updateIsLoggedInStatus(uuid, false);
        writeSessionToFile(user, "LOGOUT");

        return true;
    }
    @Override
    public Users loadCurrentSession() {
        try (BufferedReader br = new BufferedReader(new FileReader("current_session.txt"))) {
            System.out.println("Session file opened successfully.");

            String uuid = br.readLine();
            System.out.println("Read UUID from file: " + uuid);

            if (uuid == null || uuid.isBlank()) {
                System.out.println("UUID is blank or null.");
                return null;
            }

            Users user = userRepository.findByUserUuid(uuid);
            System.out.println("User found from DB: " + (user != null ? user.getUsername() : "null"));

            if (user != null) {
                System.out.println("User is logged in.");
                return user;
            }
            else {
                System.out.println("User is not logged in or does not exist.");
            }
        } catch (IOException e) {
            System.out.println("Error reading session file: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void clearCurrentSession() {
        File file = new File("current_session.txt");
        if (file.exists()) {
            file.delete();
        }
    }
}
