package utils;

import lombok.Data;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
@Data
public class DatabaseConfig {

    private static Properties properties = new Properties();

    public static Properties getDataCredential() {

        try (FileReader fileReader = new FileReader("app.properties")) {
            properties.load(fileReader);
        } catch (Exception exception) {
            System.out.println("[!] Error reading properties file" + exception.getMessage());
        }
        return properties;
    }

    public Connection getDatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    DatabaseConfig.getDataCredential().getProperty("db_url"),
                    DatabaseConfig.getDataCredential().getProperty("db_username"),
                    DatabaseConfig.getDataCredential().getProperty("db_password")
            );
            return connection;
        } catch (Exception e) {
            System.out.println("[!] Error establishing database connection: " + e.getMessage());
            e.printStackTrace(); // This will print the full stack trace
        }
        return null;
    }
    public static void main(String[] args) {
        DatabaseConfig dgConfig = new DatabaseConfig();
        try (Connection connection = dgConfig.getDatabaseConnection()) {
            System.out.println("✅ Database Connected!");
        } catch (Exception e) {
            System.out.println("❌ Database Connection Error: " + e.getMessage());
        }
    }
}