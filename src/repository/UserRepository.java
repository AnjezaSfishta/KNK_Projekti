package repository;

import models.dto.UserDTO;
import repository.interfaces.UserRepositoryInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements UserRepositoryInterface {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/admin";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "12345";
    private UserDTO user;

    public UserRepository() {
        // Initialize your repository with any necessary dependencies
    }

    @Override
    public UserDTO getUserById(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("ID");
                String username = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");

                return new UserDTO(userId, username, password);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    @Override
    public UserDTO getUserByUsername(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE USERNAME = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("ID");
                String dbUsername = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");

                return new UserDTO(userId, dbUsername, password);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    @Override
    public void addUser(UserDTO user) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO users (USERNAME, PASSWORD) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateUser(UserDTO user) {
        this.user = user;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "UPDATE users SET USERNAME = ?, PASSWORD = ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "DELETE FROM users WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public List<UserDTO> getUsersByFilter(String filter) {
        List<UserDTO> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE USERNAME LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + filter + "%");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("ID");
                String username = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");

                UserDTO user = new UserDTO(userId, username, password);
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

}

