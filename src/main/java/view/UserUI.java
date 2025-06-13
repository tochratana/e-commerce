package view;

import controller.UserController;
import model.dto.DeleteUserDto;
import model.dto.UpdateUserDto;
import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import java.util.Scanner;

public class UserUI {
    private static final UserController userController = new UserController();
    private static final Scanner scanner = new Scanner(System.in); // Single scanner instance
    private static boolean isRunning = true;

    private static void loginMenu(){
        System.out.println("============================");
        System.out.println("      User Creation    ");
        System.out.println("============================");
        System.out.println("""
                1. Register
                2. Login
                3. Exit
                """);
    }

    private static void mainMenu(){
        System.out.println("============================");
        System.out.println("       Main Menu        ");
        System.out.println("============================");
        System.out.println("""
                1. User Management
                2. Product Management
                3. Order Management
                4. Exit
                5. Logout
                """);
    }

    private static void userManagementMenu(){
        System.out.println("============================");
        System.out.println("     User Management    ");
        System.out.println("============================");
        System.out.println("""
                1. View All Users
                2. Create User
                3. Update User
                4. Find User by UUID
                5. Delete User
                6. Back to Main Menu
                """);
    }

    public static void home(){
        while(isRunning) {
            if(!userController.callUser()) {
                // User not logged in - show login menu
                loginMenu();
                handleLoginMenu();
            } else {
                // User is logged in - show main menu
                mainMenu();
                handleMainMenu();
            }
        }
        System.out.println("Thank you for using our application!");
        scanner.close();
    }

    private static void handleLoginMenu() {
        try {
            System.out.print("[+] Insert option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after nextInt()

            switch (option) {
                case 1: {
                    System.out.println("[+] Register [+]");
                    System.out.print("[+] Insert Username: ");
                    String username = scanner.nextLine();
                    System.out.print("[+] Insert Email: ");
                    String email = scanner.nextLine();
                    System.out.print("[+] Insert password: ");
                    String password = scanner.nextLine();

                    UserCreateDto userCreateDto = new UserCreateDto(username, email, password);
                    UserResponseDto user = userController.register(userCreateDto);
                    System.out.println("Registration successful!");
                    System.out.println(user);
                    break;
                }
                case 2: {
                    System.out.println("[+] Login [+] ");
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    UserResponseDto user = userController.login(email, password);
                    if(user != null) {
                        System.out.println("Login successful!");
                        System.out.println(user);
                    } else {
                        System.out.println("Login failed. Please check your credentials.");
                    }
                    break;
                }
                case 3: {
                    isRunning = false;
                    break;
                }
                default: {
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static void handleMainMenu() {
        try {
            System.out.print("[+] Insert option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline after nextInt()

            switch (option) {
                case 1: {
                    System.out.println("=== User Management ===");
                    handleUserManagement();
                    break;
                }
                case 2: {
                    System.out.println("=== Product Management ===");
                    try {
                        ProductServer productServer = new ProductServer();
                        productServer.start();
                    } catch (Exception e) {
                        System.err.println("Error starting Product Management: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    System.out.println("=== Order Management ===");
                    // TODO: Implement order management
                    System.out.println("Order Management functionality - Coming soon!");
                    break;
                }
                case 4: {
                    System.out.println("Thank you for using our system!");
                    isRunning = false;
                    break;
                }
                case 5:{
                    System.out.print("Are you sure you want to logout? (y/n):");
                    String answer = new Scanner(System.in).nextLine();
                    if(answer.equalsIgnoreCase("y")){
                        userController.logout();
                    }
                    else if(answer.equalsIgnoreCase("n")){
                        isRunning = true;
                    }
                    else {
                        System.out.println("Invalid option!");
                        return;
                    }
                }
                default: {
                    System.out.println("Invalid option. Please choose 1-5.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static void handleUserManagement() {
        boolean userManagementRunning = true;

        while (userManagementRunning) {
            try {
                userManagementMenu();
                System.out.print("[+] Insert option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (option) {
                    case 1: {
                        System.out.println("=== All Users ===");
                        userController.getAllUsers().forEach(System.out::println);
                        break;
                    }
                    case 2: {
                        System.out.println("=== Create New User ===");
                        System.out.print("[+] Insert Username: ");
                        String username = scanner.nextLine();
                        System.out.print("[+] Insert Email: ");
                        String email = scanner.nextLine();
                        System.out.print("[+] Insert password: ");
                        String password = scanner.nextLine();

                        UserCreateDto userCreateDto = new UserCreateDto(username, email, password);
                        UserResponseDto user = userController.register(userCreateDto);
                        System.out.println("User created successfully!");
                        System.out.println(user);
                        break;
                    }
                    case 3: {
                        System.out.println("=== Update User ===");
                        System.out.print("[+] Insert User UUID: ");
                        String uuid = scanner.nextLine();
                        System.out.print("[+] Insert new username: ");
                        String newUserName = scanner.nextLine();
                        System.out.print("[+] Insert new email: ");
                        String newEmail = scanner.nextLine();

                        UpdateUserDto updateUserDto = new UpdateUserDto(newUserName, newEmail);
                        UserResponseDto updatedUser = userController.updateUserByUuid(uuid, updateUserDto);

                        if (updatedUser != null) {
                            System.out.println("User updated successfully!");
                            System.out.println(updatedUser);
                        } else {
                            System.out.println("Failed to update user. Please check the UUID.");
                        }
                        break;
                    }
                    case 4: {
                        System.out.println("=== Find User by UUID ===");
                        System.out.print("[+] Insert User UUID: ");
                        String uuid = scanner.nextLine();

                        UserResponseDto user = userController.getUserByUuid(uuid);
                        if (user != null) {
                            System.out.println("User found:");
                            System.out.println(user);
                        } else {
                            System.out.println("User not found with UUID: " + uuid);
                        }
                        break;
                    }
                    case 5: {
                        System.out.println("=== Delete User ===");
                        System.out.print("[+] Insert User UUID: ");
                        String uuid = scanner.nextLine();
                        System.out.print("Are you sure you want to delete this user? (yes/no): ");
                        String confirmation = scanner.nextLine();

                        if ("yes".equalsIgnoreCase(confirmation)) {
                            DeleteUserDto deleteUserDto = new DeleteUserDto(true);
                            Integer result = userController.deleteUserByUuid(uuid, deleteUserDto);

                            if (result != null && result > 0) {
                                System.out.println("User deleted successfully!");
                            } else {
                                System.out.println("Failed to delete user. Please check the UUID.");
                            }
                        } else {
                            System.out.println("Delete operation cancelled.");
                        }
                        break;
                    }
                    case 6: {
                        System.out.println("Returning to Main Menu...");
                        userManagementRunning = false;
                        break;
                    }
                    default: {
                        System.out.println("Invalid option. Please choose 1-6.");
                        break;
                    }
                }

                // Pause before showing menu again (except when going back to main menu)
                if (userManagementRunning) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}