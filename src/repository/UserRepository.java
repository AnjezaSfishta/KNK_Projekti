package repository;

import models.User;
import services.DatabaseConnection;
import services.DatabaseHandler;
import services.PasswordHasher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private DatabaseHandler databaseHandler;
    private DatabaseConnection connection;
    private PreparedStatement pst;

    public UserRepository() {
        databaseHandler = new DatabaseHandler();
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM users WHERE username=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String saltedPassword = rs.getString("salted_password");
                String salt = rs.getString("salt");

                if (PasswordHasher.compareSaltedHash(password, salt, saltedPassword)) {
                    return new User(username, saltedPassword, salt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return null; // User not found or invalid password
    }

    // Other methods...
}
