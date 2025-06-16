import view.UserUI;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        UserUI.home();
        //System.out.println(UserUI.usernameUserLoign);

    }
}
