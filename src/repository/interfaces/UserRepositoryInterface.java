package repository.interfaces;

import models.dto.UserDTO;

import java.util.List;

public interface UserRepositoryInterface {

    UserDTO getUserById(int id);
    UserDTO getUserByUsername(String username);
    void addUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(int id);

    List<UserDTO> getUsersByFilter(String filter);
}

