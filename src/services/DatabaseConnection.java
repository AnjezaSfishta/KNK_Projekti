package services;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private final String IP = "localhost:3306";
    private final String DATATABASE = "admin";
    private final String USERNAME = "root";
    private final String PASSWORD = "12345";

    private Connection connection;


    public static DatabaseConnection getConnection() {
        return new DatabaseConnection();

    }


    public DatabaseConnection() {

        this.connection = this.innitConnection();

    }

    private Connection innitConnection() {

        try {
            Class.forName(DRIVER_NAME);
            System.out.println("Connection Okay");
            // "jdbc:mysql://{ip}/{database}" , {username}, {Password}
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/admin", "root", "12345");

        }
        catch (Exception e) {}
        System.out.println("Connetion failed...");
        return null;
    }
}
