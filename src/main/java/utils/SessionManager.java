package utils;

import lombok.Getter;
import model.dto.UserResponseDto;

public class SessionManager {
    @Getter
    private static UserResponseDto currentUser = null;

    public static void login(UserResponseDto user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
