import model.entities.Users;
import view.UserUI;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String[] args) {
        //todo: this for table not displaying
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        UserUI.home();
    }
}
