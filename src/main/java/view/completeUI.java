package view;

import controller.ProductController;
import controller.UserController;
import model.dto.DeleteUserDto;
import model.dto.UpdateUserDto;
import model.dto.UserCreateDto;
import model.dto.UserResponseDto;
import model.dto.product.ProductResponseDto;
import org.nocrala.tools.texttablefmt.Table;
import utils.SessionManager;

import java.util.List;
import java.util.Scanner;

import static view.UserUI.*;

public class completeUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isRunning = true;

    private static final UserController userController =  new UserController();
    private ProductController productController =  new ProductController();
//    private CartController cartController =  new CartController();
//    private OrderController orderController =  new OrderController();

    public void start() {}
    //todo Show Login menu ✅
    public static void showLoginMenu() {
        while(true) {
            System.out.println(completeUITable.LoginMenuUI());
            System.out.print("[+] Choose one option: ");

            int choice = getIntInput();
            switch(choice) {
                case 1:
               if(handleLogin()) {
                   showMainMenu();
                   return;
                }
               break;
                case 2:
                    if(handleRegister()){
                        showMainMenu();
                        return;
                    }
                    break;
                case 3: isRunning = false;
                break;
                default: System.out.println("[!] Invalid option. Please choose 1, 2, or 3.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }

    }
    //handle login & register

    private static boolean handleLogin(){
        System.out.println("[+] Login [+] ");
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        UserResponseDto user = userController.login(email, password);
        System.out.println(user);
        if (user != null) {
            System.out.println("[✓] Login successful! You can now login.");
            SessionManager.login(user);
            return true;
        } else {
            System.out.println("[!] Login failed! Please check your credentials.");
            return false;
        }
    }
    private static boolean handleRegister(){
        System.out.println("[+] Register [+]");
        System.out.print("[+] Insert Username: ");
        String username = scanner.nextLine();
        System.out.print("[+] Insert Email: ");
        String email = scanner.next();
        System.out.print("[+] Insert password: ");
        String password = scanner.next();

        UserCreateDto userCreateDto
                = new UserCreateDto(username, email, password);
        UserResponseDto user = userController.register(userCreateDto);
        System.out.println(user);
        if (user != null) {
            System.out.println("[✓] Registration successful! You can now login.");
            SessionManager.login(user);
            return true;

        } else {
            System.out.println("[!] Registration failed! Email might already exist.");
            return false;
        }
    }


    //show only MainMenu ✅
    public static void showMainMenu() {

        try {
            while (SessionManager.isLoggedIn()) {
                System.out.println(completeUITable.showMainMenuUI());
                System.out.print("[+] Choose one option: ");

                int choice = getIntInput();

                switch (choice) {
                    case 1 :
                        showUserMenu();
                        break;

                    case 2 :
//                        showProductMenu();
                        try {
                            ProductServer productServer = new ProductServer();
                            productServer.start();

                        } catch (Exception e) {
                            System.err.println("Error starting Product Management: " + e.getMessage());
                        }
                        break;

                    case 3 :
                        showOrderMenu();
                        System.out.println("Order Management functionality - Coming soon!");
                        break;

                    case 4 :
                        System.out.println("Thank you for using our system!");
                        isRunning = false;

                    case 5 :
                        System.out.print("Are you sure you want to logout? (y/n):");
                        String answer = new Scanner(System.in).nextLine();
                        if(answer.equalsIgnoreCase("y")){
                            SessionManager.logout();
                            System.out.println("[✓] Logged out successfully!");
                        }
                        else if(answer.equalsIgnoreCase("n")){
                            isRunning = true;
                        }
                        else {
                            System.out.println("Invalid option!");
                            return;
                        }

                    default :
                        System.out.println("[!] Invalid option.");

                }
            }
            // Add small pause before showing menu again
            if (SessionManager.isLoggedIn()) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }

        }catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            scanner.nextLine(); // Clear invalid input
        }
    }

    private static void exitApplication() {
        System.out.println("\n=== Shutting Down ===");
        System.out.println("Thank you for using Product Management Server!");
        scanner.close();
        System.exit(0);
    }

    //todo Order Menu (not yet at table)
    public static void showOrderMenu() {
        while(true) {
            System.out.println(completeUITable.showOrderMenuUI());
            System.out.print("[+] Choose option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5 : return;
                default : System.out.println("[!] Invalid option!");

            }
        }
    }

    //todo User Menu
    public static void showUserMenu() {
        while(true) {
            System.out.println(completeUITable.showUserMenuUI());
            System.out.print("[+] Choose option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1: getAllUsers(); break;
                case 2: findUserByUuid(); break;
                case 3: updateMyProfile(); break;
                case 4: deleteUser(); break;
                case 5 : return;
                default : System.out.println("[!] Invalid option!");

            }
        }
    }
    //User Management

    private static void getAllUsers() {
//        completeUITable<UserResponseDto> tableUI = new completeUITable<>();
//        System.out.println("\n===== ALL USERS =====");
//        userController.getAllUsers().forEach(System.out::println);
//        List<UserResponseDto> users = userController.getAllUsers();
//        tableUI.getUserDisplay(users);
        completeUITable<UserResponseDto> tableUI = new completeUITable<>();
        System.out.println("\n===== ALL USERS =====");

        List<UserResponseDto> users = userController.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found");
        } else {
            System.out.println(tableUI.getUserDisplay(users));
        }


        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();

    }

    private static void findUserByUuid() {
        System.out.println("\n===== FIND USER =====");
        System.out.print("[+] Enter user UUID: ");
        String uuid = scanner.nextLine();
        UserResponseDto user = userController.getUserByUuid(uuid);

        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("[!] User not found!");
        }
    }

    private static void updateMyProfile() {
//        System.out.println("\n===== UPDATE MY PROFILE =====");
//        UserResponseDto currentUser = SessionManager.getCurrentUser();
//
//        System.out.print("[+] Enter new username: ");
//        String userName = scanner.nextLine();
//        System.out.print("[+] Enter new email: ");
//        String email = scanner.nextLine();
//        System.out.print("[+] Enter new password: ");
//        String password = scanner.nextLine();
//
//        UpdateUserDto updateDto = new UpdateUserDto(userName, email, password);
//        UserResponseDto updatedUser = userController.updateUserByUuid(currentUser.uuid(), updateDto);
//
//        if (updatedUser != null) {
//            SessionManager.login(updatedUser); // Update session
//            System.out.println("[✓] Profile updated successfully!");
//            System.out.println(updatedUser);
//        } else {
//            System.out.println("[!] Failed to update profile!");
//        }
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
    }

    private static void deleteUser() {
//        System.out.println("\n===== DELETE USER =====");
//        System.out.print("[+] Enter user UUID: ");
//        String uuid = scanner.nextLine();
//        System.out.println(userController.deleteUserByUuid(uuid));

        System.out.println("\n=== Delete User ===");
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
    }
    //
 //
     /*
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

      */
//
    private static int getIntInput() {
        while(true){
            try {
//                return Integer.parseInt(scanner.nextLine());
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number format!");
                return -1;
            }
        }
    }



}
