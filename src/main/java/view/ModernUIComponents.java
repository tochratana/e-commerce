package view;

import java.util.concurrent.TimeUnit;

public class ModernUIComponents {

    // Terminal capability detection
    private static boolean supportsAnsi = true;
    private static boolean supportsUnicode = true;
    private static boolean supports256Color = true;

    static {
        detectTerminalCapabilities();
    }

    private static void detectTerminalCapabilities() {
        String term = System.getenv("TERM");
        String os = System.getProperty("os.name").toLowerCase();

        // Basic detection logic
        if (os.contains("win")) {
            // Windows terminals vary widely
            String windowsTerminal = System.getenv("WT_SESSION");
            if (windowsTerminal == null) {
                supports256Color = false; // Older Windows terminals
            }
        }

        if (term != null) {
            supportsAnsi = !term.equals("dumb");
            supports256Color = term.contains("256color") || term.contains("xterm");
        }

        // Test Unicode support by checking locale
        String encoding = System.getProperty("file.encoding");
        supportsUnicode = encoding != null &&
                (encoding.toLowerCase().contains("utf") || encoding.toLowerCase().contains("unicode"));
    }

    // Enhanced color palette with fallbacks
    public static final String RESET = supportsAnsi ? "\u001B[0m" : "";
    public static final String BOLD = supportsAnsi ? "\u001B[1m" : "";
    public static final String DIM = supportsAnsi ? "\u001B[2m" : "";
    public static final String ITALIC = supportsAnsi ? "\u001B[3m" : "";
    public static final String UNDERLINE = supportsAnsi ? "\u001B[4m" : "";

    // Color scheme with fallbacks
    public static final String PRIMARY = getColor(33, "34");      // Blue fallback
    public static final String SECONDARY = getColor(135, "35");   // Magenta fallback
    public static final String SUCCESS = getColor(82, "32");      // Green fallback
    public static final String WARNING = getColor(220, "33");     // Yellow fallback
    public static final String ERROR = getColor(196, "31");       // Red fallback
    public static final String INFO = getColor(117, "36");        // Cyan fallback
    public static final String MUTED = getColor(102, "37");       // White fallback
    public static final String ACCENT = getColor(213, "35");      // Magenta fallback

    private static String getColor(int color256, String basicColor) {
        if (!supportsAnsi) return "";
        if (supports256Color) {
            return "\u001B[38;5;" + color256 + "m";
        } else {
            return "\u001B[" + basicColor + "m";
        }
    }

    // Box drawing characters with ASCII fallbacks
    private static final String DOUBLE_TOP_LEFT = supportsUnicode ? "╔" : "+";
    private static final String DOUBLE_TOP_RIGHT = supportsUnicode ? "╗" : "+";
    private static final String DOUBLE_BOTTOM_LEFT = supportsUnicode ? "╚" : "+";
    private static final String DOUBLE_BOTTOM_RIGHT = supportsUnicode ? "╝" : "+";
    private static final String DOUBLE_HORIZONTAL = supportsUnicode ? "═" : "=";
    private static final String DOUBLE_VERTICAL = supportsUnicode ? "║" : "|";

    private static final String ROUNDED_TOP_LEFT = supportsUnicode ? "╭" : "+";
    private static final String ROUNDED_TOP_RIGHT = supportsUnicode ? "╮" : "+";
    private static final String ROUNDED_BOTTOM_LEFT = supportsUnicode ? "╰" : "+";
    private static final String ROUNDED_BOTTOM_RIGHT = supportsUnicode ? "╯" : "+";
    private static final String ROUNDED_HORIZONTAL = supportsUnicode ? "─" : "-";
    private static final String ROUNDED_VERTICAL = supportsUnicode ? "│" : "|";

    // Icons with ASCII fallbacks
    private static final String ICON_SUCCESS = supportsUnicode ? "✨" : "[OK]";
    private static final String ICON_ERROR = supportsUnicode ? "💥" : "[ERR]";
    private static final String ICON_WARNING = supportsUnicode ? "⚡" : "[!]";
    private static final String ICON_INFO = supportsUnicode ? "💡" : "[i]";
    private static final String ICON_LOADING = supportsUnicode ? "🚀" : "[...]";
    private static final String ICON_USER = supportsUnicode ? "👤" : "[U]";
    private static final String ICON_PRODUCT = supportsUnicode ? "📦" : "[P]";
    private static final String ICON_ORDER = supportsUnicode ? "🛒" : "[O]";
    private static final String ICON_LOGOUT = supportsUnicode ? "🚪" : "[OUT]";
    private static final String ICON_LOGIN = supportsUnicode ? "🔐" : "[IN]";

    public static void clearScreen() {
        if (supportsAnsi) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } else {
            // Fallback: print multiple newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public static void showWelcomeSplash(String appName, String version) {
        clearScreen();

        String[] logo = supportsUnicode ? new String[]{
                "    ███████╗██╗  ██╗ ██████╗ ██████╗   ",
                "    ██╔════╝██║  ██║██╔═══██╗██╔══██╗  ",
                "    ███████╗███████║██║   ██║██████╔╝  ",
                "    ╚════██║██╔══██║██║   ██║██╔═══╝   ",
                "    ███████║██║  ██║╚██████╔╝██║       ",
                "    ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝       "
        } : new String[]{
                "    #######  #    #  #######  ######   ",
                "    #        #    #  #     #  #     #  ",
                "    #######  ######  #     #  ######   ",
                "         #   #    #  #     #  #        ",
                "    #######  #    #  #######  #        "
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

        showAnimatedLoader("Initializing system", 3);
    }

    public static void showModernWelcomeBox(String username) {
        String message = "Welcome back, " + username + "!";
        String subtitle = "Your session has been restored";

        int contentWidth = Math.max(message.length(), subtitle.length()) + 8;
        int width = Math.max(50, Math.min(contentWidth, 70));

        System.out.print(RESET);
        System.out.println(SUCCESS + BOLD);
        System.out.println(ROUNDED_TOP_LEFT + ROUNDED_HORIZONTAL.repeat(width) + ROUNDED_TOP_RIGHT);
        System.out.println(ROUNDED_VERTICAL + " " + centerText(ICON_SUCCESS + " " + message + " " + ICON_SUCCESS, width) + " " + ROUNDED_VERTICAL);
        System.out.println(ROUNDED_VERTICAL + " " + centerText(DIM + subtitle + RESET + SUCCESS, width) + " " + ROUNDED_VERTICAL);
        System.out.println(ROUNDED_BOTTOM_LEFT + ROUNDED_HORIZONTAL.repeat(width) + ROUNDED_BOTTOM_RIGHT);
        System.out.println(RESET);
        System.out.flush();
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
            default -> supportsUnicode ? "💫" : "[*]";
        };

        String fullMessage = icon + " " + message;
        int width = Math.max(fullMessage.length() + 6, 40);

        System.out.println(color + BOLD);

        if (supportsUnicode) {
            System.out.println("▄".repeat(width + 4));
            System.out.println("██" + " ".repeat(width) + "██");
            System.out.println("██ " + centerText(fullMessage, width) + " ██");
            System.out.println("██" + " ".repeat(width) + "██");
            System.out.println("▀".repeat(width + 4));
        } else {
            // ASCII fallback
            System.out.println("+" + "-".repeat(width + 2) + "+");
            System.out.println("|" + " ".repeat(width + 2) + "|");
            System.out.println("| " + centerText(fullMessage, width) + " |");
            System.out.println("|" + " ".repeat(width + 2) + "|");
            System.out.println("+" + "-".repeat(width + 2) + "+");
        }

        System.out.println(RESET);
    }

    public static void showModernMenu(String title, String[] options, String[] icons) {
        int maxWidth = Math.max(title.length(), 40);
        for (String option : options) {
            maxWidth = Math.max(maxWidth, option.length() + 8);
        }

        // Header
        System.out.println(PRIMARY + BOLD);
        System.out.println("+" + "-".repeat(maxWidth + 4) + "+");
        System.out.println("| " + centerText((supportsUnicode ? "🌟 " : "* ") + title + (supportsUnicode ? " 🌟" : " *"), maxWidth + 2) + " |");
        System.out.println("+" + "-".repeat(maxWidth + 4) + "+");
        System.out.println(RESET);

        // Menu items
        for (int i = 0; i < options.length; i++) {
            String icon = (icons != null && i < icons.length) ? icons[i] : ">";
            String optionText = String.format("  %d. %s %s", i + 1, icon, options[i]);

            System.out.println(PRIMARY + "| " + RESET +
                    SECONDARY + optionText +
                    " ".repeat(maxWidth + 2 - optionText.length()) +
                    PRIMARY + " |" + RESET);
        }

        System.out.println(PRIMARY + "+" + "-".repeat(maxWidth + 4) + "+" + RESET);
        System.out.println();
    }

    public static void showAnimatedLoader(String message, int seconds) {
        String[] spinnerChars = supportsUnicode ?
                new String[]{"|", "/", "-", "\\", "|", "/", "-", "\\"} :
                new String[]{".", "o", "O", "o"};
        String[] dots = {"", ".", "..", "..."};

        System.out.print(INFO + BOLD + ICON_LOADING + " " + message);

        try {
            for (int i = 0; i < seconds * 8; i++) {
                System.out.print("\r" + INFO + BOLD + ICON_LOADING + " " + message +
                        dots[i % 4] + " " + spinnerChars[i % spinnerChars.length]);
                Thread.sleep(125);
            }
            System.out.println("\r" + SUCCESS + BOLD + ICON_SUCCESS + " " + message + " completed!" + RESET);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void showProgressBar(String task, int totalSteps) {
        System.out.println(INFO + BOLD + ICON_LOADING + " " + task + RESET);

        try {
            for (int i = 0; i <= totalSteps; i++) {
                int progress = (i * 100) / totalSteps;
                int barLength = 30;
                int filled = (i * barLength) / totalSteps;

                String filledChar = supportsUnicode ? "█" : "#";
                String emptyChar = supportsUnicode ? "░" : "-";
                String bar = filledChar.repeat(filled) + emptyChar.repeat(barLength - filled);

                System.out.printf("\r%s[%s] %d%% (%d/%d)%s",
                        PRIMARY, bar, progress, i, totalSteps, RESET);

                Thread.sleep(50);
            }
            System.out.println("\n" + SUCCESS + BOLD + ICON_SUCCESS + " Task completed successfully!" + RESET);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Additional utility methods for terminal compatibility
    public static void showTerminalInfo() {
        System.out.println(INFO + BOLD + "Terminal Capabilities:" + RESET);
        System.out.println("ANSI Support: " + (supportsAnsi ? SUCCESS + "YES" : ERROR + "NO") + RESET);
        System.out.println("Unicode Support: " + (supportsUnicode ? SUCCESS + "YES" : ERROR + "NO") + RESET);
        System.out.println("256 Color Support: " + (supports256Color ? SUCCESS + "YES" : ERROR + "NO") + RESET);
        System.out.println("Terminal: " + System.getenv("TERM"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("Encoding: " + System.getProperty("file.encoding"));
    }

    public static void testDisplay() {
        System.out.println("Testing display capabilities...\n");

        showGradientBox("This is a test message", "info");
        showModernMenu("Test Menu", new String[]{"Option 1", "Option 2", "Exit"}, null);

        System.out.println("Box drawing test:");
        System.out.println(DOUBLE_TOP_LEFT + DOUBLE_HORIZONTAL.repeat(20) + DOUBLE_TOP_RIGHT);
        System.out.println(DOUBLE_VERTICAL + " Unicode box test   " + DOUBLE_VERTICAL);
        System.out.println(DOUBLE_BOTTOM_LEFT + DOUBLE_HORIZONTAL.repeat(20) + DOUBLE_BOTTOM_RIGHT);

        System.out.println("\nColor test:");
        System.out.println(PRIMARY + "PRIMARY " + SECONDARY + "SECONDARY " +
                SUCCESS + "SUCCESS " + ERROR + "ERROR" + RESET);
    }

    // Utility methods remain the same
    private static String centerText(String text, int width) {
        String cleanText = text.replaceAll("\u001B\\[[;\\d]*m", "");
        int padding = Math.max(0, width - cleanText.length() - 2);
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;
        return " ".repeat(leftPad) + text + " ".repeat(rightPad);
    }

    private static String truncateString(String str, int maxLength) {
        if (str == null) return "N/A";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }

    // Your existing methods can be updated similarly...
    // (showModernReceipt, showUserCard, etc.)
}