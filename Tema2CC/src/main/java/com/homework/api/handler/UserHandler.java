package com.homework.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.Constants;
import com.homework.api.ApiUtils;
import com.homework.api.requests.CreateRequest;
import com.homework.api.requests.UpdateRequest;
import com.homework.entities.DTOs.ResponseDTO;
import com.homework.entities.DTOs.UserDTO;
import com.homework.entities.StatusCodes;
import com.homework.entities.User;
import com.homework.errors.ApplicationExceptions;
import com.homework.errors.GlobalExceptionHandler;
import com.homework.service.UserService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class UserHandler extends AbstractHandler {
    private final UserService userService;

    public UserHandler(UserService userService, ObjectMapper objectMapper, GlobalExceptionHandler globalExceptionHandler) {
        super(objectMapper, globalExceptionHandler);
        this.userService = userService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        List<String> queryComponents = ApiUtils.splitQuery(exchange.getRequestURI().toString());
        if ("GET".equals(exchange.getRequestMethod())) {
            if (queryComponents.size() == 2) {
                System.out.println("Get all users endpoint called!");
                ResponseDTO<List<User>> getAllResponse = getAll();
                exchange.getResponseHeaders().putAll(getAllResponse.getHeaders());
                exchange.sendResponseHeaders(getAllResponse.getStatusCode().getCode(), 0);
                response = super.writeResponse(getAllResponse.getBody());
            } else {
                System.out.println("Get user by ID endpoint called!");
                ResponseDTO<User> getByIdResponse = getById(queryComponents.get(2));
                if (getByIdResponse.getBody() == null)
                    throw ApplicationExceptions.notFound("User with requested ID does not exist!").get();
                exchange.getResponseHeaders().putAll(getByIdResponse.getHeaders());
                exchange.sendResponseHeaders(getByIdResponse.getStatusCode().getCode(), 0);
                response = super.writeResponse(getByIdResponse.getBody());
            }
        } else if ("POST".equals(exchange.getRequestMethod())) {
            if (queryComponents.size() == 2) {
                System.out.println("Add user endpoint called!");
                ResponseDTO<String> addUserResponse = addUser(exchange.getRequestBody());
                if (addUserResponse.getBody() == null)
                    throw ApplicationExceptions.conflict("There already is a user with the specified username in the Database!").get();
                exchange.getResponseHeaders().putAll(addUserResponse.getHeaders());
                exchange.sendResponseHeaders(addUserResponse.getStatusCode().getCode(), 0);
                response = super.writeResponse(addUserResponse.getBody());
            } else {
                throw ApplicationExceptions.methodNotAllowed(
                        "Method POST is not allowed for path with specified ID!").get();
            }
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            if (queryComponents.size() == 2) {
                throw ApplicationExceptions.methodNotAllowed(
                        "Method PUT is not allowed for path without specified ID!").get();
            } else {
                System.out.println("Update user endpoint called!");
                ResponseDTO<String> updateUserResponse = updateUser(queryComponents.get(2), exchange.getRequestBody());
                if (updateUserResponse.getBody() == null)
                    throw ApplicationExceptions.notFound("There is no user with the specified ID in the Database for update!").get();
                if (updateUserResponse.getBody().equals("CONFLICT"))
                    throw ApplicationExceptions.conflict("There is already an existent user with this username in the Database!").get();
                exchange.getResponseHeaders().putAll(updateUserResponse.getHeaders());
                exchange.sendResponseHeaders(updateUserResponse.getStatusCode().getCode(), 0);
                response = super.writeResponse(updateUserResponse.getBody());
            }
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            if (queryComponents.size() == 2) {
                throw ApplicationExceptions.methodNotAllowed(
                        "Method DELETE is not allowed for path without specified ID!").get();
            } else {
                System.out.println("Delete user endpoint called!");
                ResponseDTO<String> deleteUserResponse = deleteUser(queryComponents.get(2));
                if (deleteUserResponse.getBody() == null)
                    throw ApplicationExceptions.notFound("There is no user with the specified ID in the Database for deletion!").get();
                exchange.getResponseHeaders().putAll(deleteUserResponse.getHeaders());
                exchange.sendResponseHeaders(deleteUserResponse.getStatusCode().getCode(), 0);
                response = super.writeResponse(deleteUserResponse.getBody());
            }
        } else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseDTO<List<User>> getAll() {
        List<User> fetchedUsers = userService.getUsers();
        return new ResponseDTO<>(fetchedUsers, getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCodes.OK);
    }

    private ResponseDTO<User> getById(String requestedId) {
        User fetchedUser = userService.getUserById(requestedId);
        return new ResponseDTO<>(fetchedUser, getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCodes.OK);
    }

    private ResponseDTO<String> addUser(InputStream is) {
        CreateRequest request = super.readRequest(is, CreateRequest.class);

        UserDTO userDTO = UserDTO.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        String userId = userService.addUser(userDTO);
        return new ResponseDTO<>(userId, getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCodes.CREATED);
    }

    private ResponseDTO<String> updateUser(String id, InputStream is) {
        UpdateRequest request = super.readRequest(is, UpdateRequest.class);

        UserDTO userDTO = UserDTO.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        String userId = userService.updateUser(id, userDTO);
        return new ResponseDTO<>(userId, getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCodes.OK);
    }

    private ResponseDTO<String> deleteUser(String requestedId) {
        String userId = userService.deleteUser(requestedId);
        return new ResponseDTO<>(userId, getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCodes.OK);
    }
}
