package com.homework.service;

import com.homework.dao.IUserRepository;
import com.homework.entities.DTOs.UserDTO;
import com.homework.entities.User;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserService {
    private final IUserRepository IUserRepository;

    public List<User> getUsers() {
        return IUserRepository.getUsers();
    }

    public String addUser(UserDTO user) {
        return IUserRepository.addUser(user);
    }

    public User getUserById(String id) {
        return IUserRepository.getUserById(id);
    }

    public String updateUser(String id, UserDTO user) {
        return IUserRepository.updateUser(id, user);
    }

    public String deleteUser(String id) {
        return IUserRepository.deleteUser(id);
    }
}
