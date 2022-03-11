package com.homework.errors;

public class ConflictException extends ApplicationException {
    ConflictException(int code, String message) {
        super(code, message);
    }
}


