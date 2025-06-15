package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConfig {

    private static final String PROPERTIES_FILE = "app.properties";

    public static Connection getDatabaseConnection() {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            props.load(fis);

            String url = props.getProperty("db_url");
            String username = props.getProperty("db_username");
            String password = props.getProperty("db_password");

            return DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            System.err.println("[!] Failed to load app.properties: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[!] Failed to connect to DB: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
