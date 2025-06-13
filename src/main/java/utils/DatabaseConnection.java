package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String databaseUrl  ="jdbc:postgresql://istad_e_commerce:NK4DihgbjX8WW8LkA5E1I1Z8Mt9Pf7HG" +
            "@dpg-d12n5715pdvs73ctrn00-a.oregon-postgres.render.com/e_commerce_heqn";
    private static final String userName = "istad_e_commerce";
    private static final String password = "NK4DihgbjX8WW8LkA5E1I1Z8Mt9Pf7HG";
    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(databaseUrl, userName, password);
        }catch (Exception exception){
            System.err.println("[!] ERROR during get database connection: " + exception.getMessage());
        }
        return null;
    }
}
