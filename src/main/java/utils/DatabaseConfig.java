package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static final String databaseUrl = "jdbc:postgresql://dpg-d12n5715pdvs73ctrn00-a.oregon-postgres.render.com:5432/e_commerce_heqn";
    private static final String userName = "istad_e_commerce";
    private static final String password = "NK4DihgbjX8WW8LkA5E1I1Z8Mt9Pf7HG";

    public static Connection getDatabaseConnection() {
        try {
            return DriverManager.getConnection(databaseUrl, userName, password);
        } catch (Exception exception) {
            System.err.println("[!] ERROR during get database connection: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }
}
