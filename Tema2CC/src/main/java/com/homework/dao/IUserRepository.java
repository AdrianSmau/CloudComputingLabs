package com.homework.dao;

import com.homework.entities.DTOs.UserDTO;
import com.homework.entities.User;

import java.util.List;

public interface IUserRepository {
    List<User> getUsers();
    String addUser(UserDTO user);

    User getUserById(String id);
    String updateUser(String id, UserDTO user);
    String deleteUser(String id);
}
