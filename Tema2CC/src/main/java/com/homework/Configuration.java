package com.homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.dao.IUserRepository;
import com.homework.dao.UserRepository;
import com.homework.errors.GlobalExceptionHandler;
import com.homework.service.UserService;

public class Configuration {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final IUserRepository USER_REPOSITORY = new UserRepository();
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static UserService getUserService() {
        return USER_SERVICE;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
