package com.homework.errors;

public class NotFoundException extends ApplicationException {
    NotFoundException(int code, String message) {
        super(code, message);
    }
}

