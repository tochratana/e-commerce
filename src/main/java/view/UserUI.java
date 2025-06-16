package view;

import controller.OrderController;
import controller.ProductController;
import controller.UserController;
import model.dto.user.DeleteUserDto;
import model.dto.user.UpdateUserDto;
import model.dto.user.UserCreateDto;
import model.dto.user.UserResponseDto;
import model.entities.Users;
import model.repositories.CartRepositoryImpl;
import model.repositories.OrderRepository;
import model.repositories.OrderRepositoryImpl;
import model.service.OrderService;
import model.service.OrderServiceImpl;
import model.service.UserService;
import model.service.UserServiceImpl;
import model.service.ProductService;
import model.service.ProductServiceImpl;

import java.util.Scanner;

import static view.UIComponents.*;

public class UserUI {
    private static final UserController userController = new UserController();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isRunning = true;
    public static String usernameUserLoign;


    // Initialize dependencies in the correct order
    private static final CartRepositoryImpl cartRepositoryImpl = new CartRepositoryImpl();
    private static final OrderRepository orderRepository = new OrderRepositoryImpl();
    private static final UserService userService = new UserServiceImpl();
    private static final ProductService productService = new ProductServiceImpl();
    private static final OrderService orderService = new OrderServiceImpl(cartRepositoryImpl, orderRepository, userService, productService);
    private static final OrderController orderController = new OrderController(orderService);
    private static final OrderUI orderUI = new OrderUI(orderController,new ProductController(),productService); // ✅ Pass OrderController to OrderUI

    private static UserResponseDto loggedInUser; // Class-level variable

    private static void loginMenu() {
        ModernUIComponents.showWelcomeSplash("E-Commerce","1.0.0");
        System.out.println(completeUITable.LoginMenuUI());
    }

    private static void mainMenu() {
        System.out.println(completeUITable.showMainMenuUI());
    }

    private static void userManagementMenu() {
        System.out.println(completeUITable.showUserMenuUI());
    }

    public static void home() {
        // restore session on startup
        loadSessionOnStartup();
        while (isRunning) {
            if (loggedInUser == null) {
                loginMenu();
                handleLoginMenu();
            } else {
                mainMenu();
                handleMainMenu();
            }
        }
        System.out.println("Thank you for using our application!");
        scanner.close();
    }

    private static void loadSessionOnStartup() {
        try {
            UserService userService = new UserServiceImpl();
            Users sessionUser = userService.loadCurrentSession();
            if (sessionUser != null) {
                loggedInUser = new UserResponseDto(
                        sessionUser.getId(),
                        sessionUser.getUsername(),
                        sessionUser.getEmail(),
                        sessionUser.getUuid()
                );

                // Just show the modern welcome box - remove duplicate messages
                ModernUIComponents.showModernWelcomeBox(loggedInUser.username());
            }
        } catch (Exception e) {
            System.out.println(RED+ "[!] No previous session found or session expired."+RESET);
            loggedInUser = null;
        }
    }

    private static void handleLoginMenu() {
        try {
            System.out.print("[+] Insert option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("[+] Register [+]");
                    System.out.print("[+] Insert Username: ");
                    usernameUserLoign = scanner.nextLine();
                    //email validation
                    String email;
                    while (true) {
                        System.out.print("[+] Insert Email: ");
                        email = scanner.nextLine();
                        if (email.contains(".com")) {
                            break; // valid email
                        } else {
                            System.out.println(RED+ "[!] Invalid email. Email must contain '.com'. Please try again." + RESET);
                        }
                    }
                    System.out.print("[+] Insert password: ");
                    String password = scanner.nextLine();

                    UserCreateDto userCreateDto = new UserCreateDto(usernameUserLoign, email, password);
                    UserResponseDto user = userController.register(userCreateDto);
                    System.out.println("Registration successful!");
                    System.out.println(user);
                }
                case 2 -> {
                    System.out.println("[+] Login [+]");
                    //email validation
                    String email;
                    while (true) {
                        System.out.print("[+] Enter Email: ");
                        email = scanner.nextLine();
                        if (email.contains(".com")) {
                            break; // valid email
                        } else {
                            System.out.println(RED+ "[!] Invalid email. Email must contain '.com'. Please try again." + RESET);
                        }
                    }
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    UserResponseDto user = userController.login(email, password);
                    if (user != null) {
                        loggedInUser = user; // ✅ Correct use
                        ModernUIComponents.showModernWelcomeBox(user.username());
                        System.out.println(GREEN+"Login successful!"+RESET);
                        System.out.println(user);
                    } else {
                        System.out.println("Login failed. Please check your credentials.");
                    }
                }
                case 3 -> isRunning = false;
                default -> System.out.println(RED+"[!] Invalid option. Please choose 1, 2, or 3."+RESET);
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            scanner.nextLine(); // Clear input
        }
    }

    private static void handleMainMenu() {
        try {
            System.out.print("[+] Insert option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> handleUserManagement();
                case 2 -> {
                    try {
                        ProductUI productServer = new ProductUI();
                        productServer.start();
                    } catch (Exception e) {
                        System.err.println("Error starting Product Management: " + e.getMessage());
                    }
                }
                case 3 -> {
                    if (loggedInUser != null) {
                        orderUI.start(loggedInUser.id()); // Pass correct user ID
                    } else {
                        System.out.println(RED+"[!] You need to log in to access order management."+RESET);
                    }
                }
                case 4 -> {
                    System.out.println(RED+"Logging out..."+RESET);
                    UserService userService = new UserServiceImpl();
                    boolean logoutSuccess = userService.logout(); //  Use the service logout method
                    if (logoutSuccess) {
                        loggedInUser = null; // Clear the UI session
                        System.out.println(GREEN+"Logged out successfully!"+RESET);
                    } else {
                        System.out.println(RED+"[!] Logout failed." +RESET);
                    }
                }
                case 5 -> {
                    System.out.println(GREEN+"Thank you for using our system!"+RESET);
                    isRunning = false;
                }
                default -> System.out.println(RED+"[!] Invalid option. Please choose 1-5."+RESET);
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            scanner.nextLine();
        }
    }

    private static void handleUserManagement() {
        boolean userManagementRunning = true;

        while (userManagementRunning) {
            try {
                userManagementMenu();
                System.out.print("[+] Insert option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1 -> {
                        TableUI<UserResponseDto> tableUI = new TableUI<>();
                        tableUI.getTableDisplay(userController.getAllUsers());
                        //userController.getAllUsers();
                    }
                    case 2 -> {
                        System.out.println(PURPLE+"=== Create New User ==="+RESET);
                        System.out.print("[+] Insert Username: ");
                        String username = scanner.nextLine();
                        System.out.print("[+] Insert Email: ");
                        String email = scanner.nextLine();
                        System.out.print("[+] Insert password: ");
                        String password = scanner.nextLine();

                        UserCreateDto userCreateDto = new UserCreateDto(username, email, password);
                        UserResponseDto user = userController.register(userCreateDto);
                        System.out.println(GREEN+"User created successfully!"+RESET);
                        System.out.println(user);
                    }
                    case 3 -> {
                        System.out.println(PURPLE+"=== Update User ==="+RESET);
                        System.out.print("[+] Insert User UUID: ");
                        String uuid = scanner.nextLine();
                        System.out.print("[+] Insert new username: ");
                        String newUserName = scanner.nextLine();
                        System.out.print("[+] Insert new email: ");
                        String newEmail = scanner.nextLine();

                        UpdateUserDto updateUserDto = new UpdateUserDto(newUserName, newEmail);
                        UserResponseDto updatedUser = userController.updateUserByUuid(uuid, updateUserDto);

                        if (updatedUser != null) {
                            System.out.println(GREEN+"User updated successfully!"+RESET);
                            System.out.println(updatedUser);
                        } else {
                            System.out.println("Failed to update user. Please check the UUID.");
                        }
                    }
                    case 4 -> {
                        System.out.println(PURPLE+"=== Find User by UUID ==="+RESET);
                        System.out.print("[+] Insert User UUID: ");
                        String uuid = scanner.nextLine();

                        UserResponseDto user = userController.getUserByUuid(uuid);
                        if (user != null) {
                            System.out.println(GREEN+"User found:"+RESET);
                            System.out.println(user);
                        } else {
                            System.out.println(RED+"[!] User not found with UUID: " + uuid+RESET);
                        }
                    }
                    case 5 -> {
                        System.out.println(PURPLE+"=== Delete User ==="+RESET);
                        System.out.print("[+] Insert User UUID: ");
                        String uuid = scanner.nextLine();
                        System.out.print(RED+"Are you sure you want to delete this user? (yes/no): "+RESET);
                        String confirmation = scanner.nextLine();

                        if ("yes".equalsIgnoreCase(confirmation)) {
                            DeleteUserDto deleteUserDto = new DeleteUserDto(true);
                            Integer result = userController.deleteUserByUuid(uuid, deleteUserDto);

                            if (result != null && result > 0) {
                                System.out.println(GREEN+"User deleted successfully!"+RESET);
                            } else {
                                System.out.println(RED+"[!] Failed to delete user. Please check the UUID."+RESET);
                            }
                        } else {
                            System.out.println(RED+"[!] Delete operation cancelled."+RESET);
                        }
                    }
                    case 6 -> userManagementRunning = false;
                    default -> System.out.println(RED+"[!] Invalid option. Please choose 1-6."+RESET);
                }

                if (userManagementRunning) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }
}