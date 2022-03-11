package com.homework.dao;

import com.homework.entities.DTOs.UserDTO;
import com.homework.entities.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.homework.Constants.DATABASE_URL;

public class UserRepository implements IUserRepository {
    private static Connection conn = null;

    static {
        createUsersTable();
    }

    public static void createUsersTable() {
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS users (\n" +
                    "id text PRIMARY KEY,\n" +
                    "username text NOT NULL,\n" +
                    "password text\n" +
                    ");");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> fetchedUsers = new LinkedList<>();
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, username, password FROM users");
            while (resultSet.next()) {
                fetchedUsers.add(User.builder()
                        .id(resultSet.getString("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .build());
            }
            System.out.println("Fetched all the users - a total of " + fetchedUsers.size() + "!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return fetchedUsers;
    }

    @Override
    public String addUser(UserDTO user) {
        if (getUserByUsername(user.getUsername()) != null)
            return null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            String id = UUID.randomUUID().toString();
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO users(id, username, password) VALUES(?,?,?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            System.out.println("Added user with ID " + id + "!");
            return id;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return null;
    }

    @Override
    public User getUserById(String id) {
        User fetchedUser = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT id, username, password FROM users WHERE id = ?");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                fetchedUser = User.builder()
                        .id(resultSet.getString("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .build();
            }
            if (fetchedUser != null)
                System.out.println("Fetched user named " + fetchedUser.getUsername() + "!");
            return fetchedUser;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return null;
    }

    @Override
    public String updateUser(String id, UserDTO user) {
        if (getUserById(id) == null)
            return null;
        User fetchedUserByUsername = getUserByUsername(user.getUsername());
        if (fetchedUserByUsername != null && !fetchedUserByUsername.getId().equals(id))
            return "CONFLICT";
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET username = ? , password = ? WHERE id = ?");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, id);
            preparedStatement.executeUpdate();
            System.out.println("Updated user with ID " + id + "!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return id;
    }

    @Override
    public String deleteUser(String id) {
        if (getUserById(id) == null)
            return null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM users WHERE id = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Deleted user with ID " + id + "!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return id;
    }

    private User getUserByUsername(String username) {
        User fetchedUser = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT id, username, password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                fetchedUser = User.builder()
                        .id(resultSet.getString("id"))
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .build();
            }
            return fetchedUser;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return null;
    }
}
