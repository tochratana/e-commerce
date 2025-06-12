package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String databaseUrl  ="jdbc:postgresql://localhost:5432/postgres";
    private static final String userName = "postgres";
    private static final String password = "qwer";
    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(databaseUrl, userName, password);
        }catch (Exception exception){
            System.err.println("[!] ERROR during get database connection: " + exception.getMessage());
        }
        return null;
    }
}
