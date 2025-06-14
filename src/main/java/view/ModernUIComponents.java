package view;

import java.util.concurrent.TimeUnit;

public class ModernUIComponents {

    // Enhanced color palette with more modern colors
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String BLINK = "\u001B[5m";
    public static final String REVERSE = "\u001B[7m";
    public static final String STRIKETHROUGH = "\u001B[9m";

    // Modern color scheme
    public static final String PRIMARY = "\u001B[38;5;33m";      // Modern blue
    public static final String SECONDARY = "\u001B[38;5;135m";   // Purple
    public static final String SUCCESS = "\u001B[38;5;82m";      // Bright green
    public static final String WARNING = "\u001B[38;5;220m";     // Gold
    public static final String ERROR = "\u001B[38;5;196m";       // Bright red
    public static final String INFO = "\u001B[38;5;117m";        // Light blue
    public static final String MUTED = "\u001B[38;5;102m";       // Gray
    public static final String ACCENT = "\u001B[38;5;213m";      // Pink

    // Background colors
    public static final String BG_PRIMARY = "\u001B[48;5;33m";
    public static final String BG_SUCCESS = "\u001B[48;5;82m";
    public static final String BG_WARNING = "\u001B[48;5;220m";
    public static final String BG_ERROR = "\u001B[48;5;196m";
    public static final String BG_DARK = "\u001B[48;5;236m";

    // Modern box drawing characters
    private static final String DOUBLE_TOP_LEFT = "╔";
    private static final String DOUBLE_TOP_RIGHT = "╗";
    private static final String DOUBLE_BOTTOM_LEFT = "╚";
    private static final String DOUBLE_BOTTOM_RIGHT = "╝";
    private static final String DOUBLE_HORIZONTAL = "═";
    private static final String DOUBLE_VERTICAL = "║";

    private static final String ROUNDED_TOP_LEFT = "╭";
    private static final String ROUNDED_TOP_RIGHT = "╮";
    private static final String ROUNDED_BOTTOM_LEFT = "╰";
    private static final String ROUNDED_BOTTOM_RIGHT = "╯";
    private static final String ROUNDED_HORIZONTAL = "─";
    private static final String ROUNDED_VERTICAL = "│";

    // Modern icons and symbols
    private static final String ICON_SUCCESS = "✨";
    private static final String ICON_ERROR = "💥";
    private static final String ICON_WARNING = "⚡";
    private static final String ICON_INFO = "💡";
    private static final String ICON_LOADING = "🚀";
    private static final String ICON_USER = "👤";
    private static final String ICON_PRODUCT = "📦";
    private static final String ICON_ORDER = "🛒";
    private static final String ICON_LOGOUT = "🚪";
    private static final String ICON_LOGIN = "🔐";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void showWelcomeSplash(String appName, String version) {
        clearScreen();

        String[] logo = {
                "    ███████╗██╗  ██╗ ██████╗ ██████╗   ",
                "    ██╔════╝██║  ██║██╔═══██╗██╔══██╗  ",
                "    ███████╗███████║██║   ██║██████╔╝  ",
                "    ╚════██║██╔══██║██║   ██║██╔═══╝   ",
                "    ███████║██║  ██║╚██████╔╝██║       ",
                "    ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝       "
        };

        int width = 60;

        System.out.println(PRIMARY + BOLD);
        System.out.println(DOUBLE_TOP_LEFT + DOUBLE_HORIZONTAL.repeat(width) + DOUBLE_TOP_RIGHT);

        for (String line : logo) {
            System.out.println(DOUBLE_VERTICAL + centerText(line, width) + DOUBLE_VERTICAL);
        }

        System.out.println(DOUBLE_VERTICAL + centerText("", width) + DOUBLE_VERTICAL);
        System.out.println(DOUBLE_VERTICAL + centerText(BOLD + appName + " V" + version, width) + DOUBLE_VERTICAL);
        System.out.println(DOUBLE_VERTICAL + centerText(DIM + "Modern E-Commerce Management System", width) + DOUBLE_VERTICAL);
        System.out.println(DOUBLE_VERTICAL + centerText("", width) + DOUBLE_VERTICAL);

        System.out.println(DOUBLE_BOTTOM_LEFT + DOUBLE_HORIZONTAL.repeat(width) + DOUBLE_BOTTOM_RIGHT);
        System.out.println(RESET);

        // Animated loading
        showAnimatedLoader("Initializing system", 3);
    }



    public static void showModernWelcomeBox(String username) {
        String message = "Welcome back, " + username + "!";
        String subtitle = "Your session has been restored";

        int contentWidth = Math.max(message.length(), subtitle.length()) + 8;
        int width = Math.max(50, Math.min(contentWidth, 70)); // Better min/max range

        // Make sure we start fresh and end clean
        System.out.print(RESET); // Reset before starting
        System.out.println(SUCCESS + BOLD);
        System.out.println(ROUNDED_TOP_LEFT + ROUNDED_HORIZONTAL.repeat(width) + ROUNDED_TOP_RIGHT);
        System.out.println(ROUNDED_VERTICAL + " " + centerText(ICON_SUCCESS + " " + message + " " + ICON_SUCCESS, width) + " " + ROUNDED_VERTICAL);
        System.out.println(ROUNDED_VERTICAL + " " + centerText(DIM + subtitle + RESET + SUCCESS, width) + " " + ROUNDED_VERTICAL); // Reset DIM properly
        System.out.println(ROUNDED_BOTTOM_LEFT + ROUNDED_HORIZONTAL.repeat(width) + ROUNDED_BOTTOM_RIGHT);
        System.out.println(RESET); // Final reset
        System.out.flush(); // Ensure output is flushed
    }



    private static String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    public static void showGradientBox(String message, String type) {
        String color = switch (type.toLowerCase()) {
            case "success" -> SUCCESS;
            case "error" -> ERROR;
            case "warning" -> WARNING;
            case "info" -> INFO;
            default -> PRIMARY;
        };

        String icon = switch (type.toLowerCase()) {
            case "success" -> ICON_SUCCESS;
            case "error" -> ICON_ERROR;
            case "warning" -> ICON_WARNING;
            case "info" -> ICON_INFO;
            default -> "💫";
        };

        String fullMessage = icon + " " + message;
        int width = Math.max(fullMessage.length() + 6, 40);

        System.out.println(color + BOLD);
        System.out.println("▄".repeat(width + 4));
        System.out.println("██" + " ".repeat(width) + "██");
        System.out.println("██ " + centerText(fullMessage, width) + " ██");
        System.out.println("██" + " ".repeat(width) + "██");
        System.out.println("▀".repeat(width + 4));
        System.out.println(RESET);
    }

    public static void showModernMenu(String title, String[] options, String[] icons) {
        int maxWidth = Math.max(title.length(), 40);
        for (String option : options) {
            maxWidth = Math.max(maxWidth, option.length() + 8);
        }

        // Header
        System.out.println(PRIMARY + BOLD);
        System.out.println("┌" + "─".repeat(maxWidth + 4) + "┐");
        System.out.println("│ " + centerText("🌟 " + title + " 🌟", maxWidth + 2) + " │");
        System.out.println("├" + "─".repeat(maxWidth + 4) + "┤");
        System.out.println(RESET);

        // Menu items with hover effect simulation
        for (int i = 0; i < options.length; i++) {
            String icon = (icons != null && i < icons.length) ? icons[i] : "▶";
            String optionText = String.format("  %d. %s %s", i + 1, icon, options[i]);

            System.out.println(PRIMARY + "│ " + RESET +
                    SECONDARY + optionText +
                    " ".repeat(maxWidth + 2 - optionText.length()) +
                    PRIMARY + " │" + RESET);
        }

        System.out.println(PRIMARY + "└" + "─".repeat(maxWidth + 4) + "┘" + RESET);
        System.out.println();
    }

    public static void showAnimatedLoader(String message, int seconds) {
        String[] spinnerChars = {"|", "/", "-", "\\", "|", "/", "-", "\\"};
        String[] dots = {"", ".", "..", "..."};

        System.out.print(INFO + BOLD + ICON_LOADING + " " + message);

        try {
            for (int i = 0; i < seconds * 8; i++) {
                System.out.print("\r" + INFO + BOLD + ICON_LOADING + " " + message +
                        dots[i % 4] + " " + spinnerChars[i % 8]);
                Thread.sleep(125);
            }
            System.out.println("\r" + SUCCESS + BOLD + ICON_SUCCESS + " " + message + " completed!" + RESET);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void showProgressBar(String task, int totalSteps) {
        System.out.println(INFO + BOLD + "🚀 " + task + RESET);

        try {
            for (int i = 0; i <= totalSteps; i++) {
                int progress = (i * 100) / totalSteps;
                int barLength = 30;
                int filled = (i * barLength) / totalSteps;

                String bar = "█".repeat(filled) + "░".repeat(barLength - filled);

                System.out.printf("\r%s[%s] %d%% (%d/%d)%s",
                        PRIMARY, bar, progress, i, totalSteps, RESET);

                Thread.sleep(50);
            }
            System.out.println("\n" + SUCCESS + BOLD + "✨ Task completed successfully!" + RESET);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void showModernReceipt(model.dto.order.OrderDTO order) {
        int width = 65;

        System.out.println(SUCCESS + BOLD);
        System.out.println("╔" + "═".repeat(width) + "╗");
        System.out.println("║" + centerText("🧾 DIGITAL RECEIPT 🧾", width) + "║");
        System.out.println("╠" + "═".repeat(width) + "╣");

        // Order details with modern styling
        System.out.printf("║ 📋 Order Code: %s%-45s ║%n",
                ACCENT, order.orderCode() + RESET + SUCCESS);
        System.out.printf("║ 📅 Date: %s%-51s ║%n",
                ACCENT, order.orderDate() + RESET + SUCCESS);
        System.out.println("╠" + "═".repeat(width) + "╣");

        // Header for items
        System.out.printf("║ %-20s %8s %12s %15s ║%n",
                "📦 Product", "Qty", "💰 Price", "💵 Subtotal");
        System.out.println("╠" + "─".repeat(width) + "╣");

        // Items with alternating colors
        boolean alternate = false;
        for (model.dto.order.OrderItemDto item : order.items()) {
            double subtotal = item.productPrice() * item.quantity();
            String color = alternate ? MUTED : "";

            System.out.printf("║%s %-20s %8d $%11.2f $%14.2f %s║%n",
                    color,
                    truncateString(item.productName(), 20),
                    item.quantity(),
                    item.productPrice(),
                    subtotal,
                    color.isEmpty() ? "" : RESET + SUCCESS);

            alternate = !alternate;
        }

        System.out.println("╠" + "═".repeat(width) + "╣");

        // Totals with emphasis
        System.out.printf("║ %s📊 Total Items: %-44d%s ║%n",
                ACCENT, order.totalQuantity(), RESET + SUCCESS);
        System.out.printf("║ %s💎 TOTAL AMOUNT: $%-41.2f%s ║%n",
                ACCENT + BOLD, order.totalPrice(), RESET + SUCCESS);

        System.out.println("╚" + "═".repeat(width) + "╝");
        System.out.println(RESET);

        // Thank you message
        showGradientBox("Thank you for your order! 🎉", "success");
    }

    public static void showUserCard(model.dto.user.UserResponseDto user, String title) {
        int cardWidth = 50;

        System.out.println(PRIMARY + BOLD);
        System.out.println("┌" + "─".repeat(cardWidth) + "┐");
        System.out.println("│" + centerText("👤 " + title, cardWidth) + "│");
        System.out.println("├" + "─".repeat(cardWidth) + "┤");

        // User details with icons
        System.out.printf("│ %s🆔 ID: %s%-38s%s │%n",
                INFO, ACCENT, user.id(), RESET + PRIMARY);
        System.out.printf("│ %s👤 Username: %s%-32s%s │%n",
                INFO, ACCENT, user.username(), RESET + PRIMARY);
        System.out.printf("│ %s📧 Email: %s%-35s%s │%n",
                INFO, ACCENT, user.email(), RESET + PRIMARY);
        System.out.printf("│ %s🔑 UUID: %s%-36s%s │%n",
                INFO, ACCENT, truncateString(user.uuid(), 32), RESET + PRIMARY);

        System.out.println("└" + "─".repeat(cardWidth) + "┘");
        System.out.println(RESET);
    }

    public static void showSystemStatus() {
        System.out.println(INFO + BOLD + "🖥️  System Status" + RESET);
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Simulated system metrics
        showStatusLine("Memory Usage", "2.1GB / 8GB", 26, SUCCESS);
        showStatusLine("CPU Usage", "15%", 15, SUCCESS);
        showStatusLine("Database", "Connected", 100, SUCCESS);
        showStatusLine("Active Sessions", "1", 100, INFO);

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    private static void showStatusLine(String label, String value, int percentage, String color) {
        int barWidth = 20;
        int filled = (percentage * barWidth) / 100;
        String bar = "█".repeat(filled) + "░".repeat(barWidth - filled);

        System.out.printf("%-15s [%s%s%s] %s%s%s%n",
                label, color, bar, RESET, color, value, RESET);
    }

    public static void showConfirmationDialog(String message, String type) {
        String color = type.equals("danger") ? ERROR : WARNING;
        String icon = type.equals("danger") ? "⚠️" : "❓";

        int width = Math.max(message.length() + 10, 50);

        System.out.println(color + BOLD);
        System.out.println("╔" + "═".repeat(width) + "╗");
        System.out.println("║" + centerText(icon + " CONFIRMATION", width) + "║");
        System.out.println("╠" + "═".repeat(width) + "╣");
        System.out.println("║" + centerText(message, width) + "║");
        System.out.println("║" + centerText("", width) + "║");
        System.out.println("║" + centerText("Continue? (y/N)", width) + "║");
        System.out.println("╚" + "═".repeat(width) + "╝");
        System.out.println(RESET);
    }

    public static void showTypingEffect(String text, int delayMs) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println();
    }

    public static void pauseWithStyle() {
        System.out.println(MUTED + "\n⏸️  Press Enter to continue..." + RESET);
    }

    public static void showSeparator(String style) {
        String sep = switch (style.toLowerCase()) {
            case "double" -> "═".repeat(65);
            case "dotted" -> "·".repeat(65);
            case "wave" -> "~".repeat(65);
            default -> "─".repeat(65);
        };
        System.out.println(MUTED + sep + RESET);
    }

    // Utility methods
    private static String centerText(String text, int width) {
        // Remove any ANSI codes when calculating actual text length
        String cleanText = text.replaceAll("\u001B\\[[;\\d]*m", "");
        int padding = Math.max(0, width - cleanText.length() - 2); // -2 for border chars
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;

        return " ".repeat(leftPad) + text + " ".repeat(rightPad);
    }

    private static String truncateString(String str, int maxLength) {
        if (str == null) return "N/A";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    // Color test method
    public static void showColorPalette() {
        System.out.println(BOLD + "🎨 Modern Color Palette:" + RESET);
        System.out.println(PRIMARY + "█ PRIMARY  " + RESET +
                SECONDARY + "█ SECONDARY  " + RESET +
                SUCCESS + "█ SUCCESS" + RESET);
        System.out.println(WARNING + "█ WARNING  " + RESET +
                ERROR + "█ ERROR    " + RESET +
                INFO + "█ INFO" + RESET);
        System.out.println(MUTED + "█ MUTED    " + RESET +
                ACCENT + "█ ACCENT" + RESET);
    }
}
