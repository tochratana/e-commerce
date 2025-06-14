package view;

public class UIComponents {

    // Color codes for console
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    // Box characters
    private static final String TOP_LEFT = "╔";
    private static final String TOP_RIGHT = "╗";
    private static final String BOTTOM_LEFT = "╚";
    private static final String BOTTOM_RIGHT = "╝";
    private static final String HORIZONTAL = "═";
    private static final String VERTICAL = "║";

    public static void showWelcomeBox(String username) {
        String message = "Welcome back, " + username + "!";
        String subtitle = "Your session has been restored";

        int width = Math.max(message.length(), subtitle.length()) + 4;

        System.out.println(GREEN + BOLD);
        System.out.println(TOP_LEFT + HORIZONTAL.repeat(width) + TOP_RIGHT);
        System.out.println(VERTICAL + " " + centerText("🎉 " + message + " 🎉", width) + " " + VERTICAL);
        System.out.println(VERTICAL + " " + centerText(subtitle, width) + " " + VERTICAL);
        System.out.println(BOTTOM_LEFT + HORIZONTAL.repeat(width) + BOTTOM_RIGHT);
        System.out.println(RESET);
    }

    public static void showSuccessBox(String message) {
        showColoredBox(message, GREEN, "✅");
    }

    public static void showErrorBox(String message) {
        showColoredBox(message, RED, "❌");
    }

    public static void showInfoBox(String message) {
        showColoredBox(message, BLUE, "ℹ️");
    }

    public static void showWarningBox(String message) {
        showColoredBox(message, YELLOW, "⚠️");
    }

    private static void showColoredBox(String message, String color, String icon) {
        String fullMessage = icon + " " + message;
        int width = fullMessage.length() + 4;

        System.out.println(color + BOLD);
        System.out.println(TOP_LEFT + HORIZONTAL.repeat(width) + TOP_RIGHT);
        System.out.println(VERTICAL + " " + centerText(fullMessage, width) + " " + VERTICAL);
        System.out.println(BOTTOM_LEFT + HORIZONTAL.repeat(width) + BOTTOM_RIGHT);
        System.out.println(RESET);
    }

    public static void showMenuBox(String title, String[] options) {
        int maxWidth = title.length();
        for (String option : options) {
            maxWidth = Math.max(maxWidth, option.length());
        }
        maxWidth += 6; // Add padding

        System.out.println(CYAN + BOLD);
        System.out.println(TOP_LEFT + HORIZONTAL.repeat(maxWidth) + TOP_RIGHT);
        System.out.println(VERTICAL + " " + centerText(title, maxWidth) + " " + VERTICAL);
        System.out.println(VERTICAL + HORIZONTAL.repeat(maxWidth) + VERTICAL);

        for (int i = 0; i < options.length; i++) {
            String option = String.format("  %d. %s", i + 1, options[i]);
            System.out.println(VERTICAL + " " + padRight(option, maxWidth) + " " + VERTICAL);
        }

        System.out.println(BOTTOM_LEFT + HORIZONTAL.repeat(maxWidth) + BOTTOM_RIGHT);
        System.out.println(RESET);
    }

    public static void showLoadingBar(String message, int totalSteps) {
        System.out.print(BLUE + message + " [");
        for (int i = 0; i < totalSteps; i++) {
            System.out.print("█");
            try {
                Thread.sleep(100); // Simulate loading
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("] Complete!" + RESET);
    }

    public static void showReceipt(model.dto.order.OrderDTO order) {
        System.out.println(GREEN + BOLD);
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        🧾 ORDER RECEIPT                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Order Code: %-47s ║%n", order.orderCode());
        System.out.printf("║ Date: %-55s ║%n", order.orderDate());
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s %8s %10s %15s ║%n", "Product", "Qty", "Price", "Subtotal");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");

        for (model.dto.order.OrderItemDto item : order.items()) {
            double subtotal = item.productPrice() * item.quantity();
            System.out.printf("║ %-20s %8d $%9.2f $%14.2f ║%n",
                    truncateString(item.productName(), 20),
                    item.quantity(),
                    item.productPrice(),
                    subtotal);
        }

        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Items: %-47d ║%n", order.totalQuantity());
        System.out.printf("║ TOTAL PRICE: $%-46.2f ║%n", order.totalPrice());
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);
    }

    public static void clearScreen() {
        System.out.print("\033[2J\033[H");
    }

    public static void showSeparator() {
        System.out.println(CYAN + "═".repeat(65) + RESET);
    }

    public static void pauseForUser() {
        System.out.println(YELLOW + "\n⏸️  Press Enter to continue..." + RESET);
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, width - text.length() - padding));
    }

    private static String padRight(String text, int width) {
        return text + " ".repeat(Math.max(0, width - text.length()));
    }

    private static String truncateString(String str, int maxLength) {
        if (str == null) return "N/A";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}