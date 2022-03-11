package com.homework.errors;

public class BadRequestException extends ApplicationException {
    BadRequestException(int code, String message) {
        super(code, message);
    }
}





