
import view.ProductServer;
import view.UserUI;
import view.completeUI;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
//        UserUI.home();
        completeUI.showLoginMenu();


//        ProductServer productServer = new ProductServer();
//        productServer.start();
    }
}