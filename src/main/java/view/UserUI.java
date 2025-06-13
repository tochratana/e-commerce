package view;

import controller.UserController;
import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import java.util.Scanner;

public class UserUI {
    private static final UserController userController
            = new UserController();
    private static void thumbnail(){
        System.out.println("============================");
        System.out.println("      User Creation    ");
        System.out.println("============================");
        System.out.println("""
                1. Register
                2. Login
                """);
    }

    public static void home(){
        thumbnail();
        if(!userController.callUser()) {
            System.out.print("[+] Insert option: ");
            switch (new Scanner(System.in).nextInt()) {
                case 1: {
                    System.out.println("[+] Register [+]");
                    System.out.print("[+] Insert Username: ");
                    String username = new Scanner(System.in).nextLine();
                    System.out.print("[+] Insert Email: ");
                    String email = new Scanner(System.in).next();
                    System.out.print("[+] Insert password: ");
                    String password = new Scanner(System.in).next();
                    UserCreateDto userCreateDto
                            = new UserCreateDto(username, email, password);
                    UserResponseDto user = userController.register(userCreateDto);
                    System.out.println(user);
                }
                case 2: {
                    System.out.println("[+] Login [+] ");
                    System.out.print("Enter email: ");
                    String email = new Scanner(System.in).next();
                    System.out.print("Enter password: ");
                    String password = new Scanner(System.in).next();
                    UserResponseDto user = userController.login(email, password);
                    System.out.println(user);
                    userController.callUser();
                }
            }
        }
        /*while (true){
            thumbnail();
            System.out.print("[+] Insert option: ");
            switch (new Scanner(System.in).nextInt()){
                case 1->{
                    userController.getAllUsers().forEach(System.out::println);
                }
                case 2->{
                    System.out.print("[+] Insert Username: ");
                    String username = new Scanner(System.in).nextLine();
                    System.out.print("[+] Insert Email: ");
                    String email = new Scanner(System.in).next();
                    System.out.print("[+] Insert password: ");
                    String password = new Scanner(System.in).next();
                    UserCreateDto userCreateDto
                            = new UserCreateDto(username, email, password);
                    UserResponseDto user = userController.register(userCreateDto);
                    System.out.println(user);
                }
                case 3->{
                    System.out.print("[+] Insert User Uuid: ");
                    String uuid = new Scanner(System.in).nextLine();
                    System.out.print("[+] Insert new username: ");
                    String newUserName = new Scanner(System.in).nextLine();
                    System.out.print("[+] Insert new email: ");
                    String newEmail = new Scanner(System.in).next();
                    UserResponseDto updatedUser = userController
                            .updateUserByUuid(uuid,
                                    new UpdateUserDto(newUserName,
                                            newEmail));
                    System.out.println(updatedUser);
                }
                case 4->{
                    System.out.print("[+] Insert User uuid: ");
                    String uuid = new Scanner(System.in).nextLine();
                    System.out.println(userController.getUserByUuid(uuid));
                }
                case 5->{
                    boolean isDeleted = true;
                    System.out.print("[+] Insert User uuid: ");
                    String uuid = new Scanner(System.in).next();
                    userController.deleteUserByUuid(uuid,new DeleteUserDto(isDeleted));
                }
            }
        }*/
    }
}
