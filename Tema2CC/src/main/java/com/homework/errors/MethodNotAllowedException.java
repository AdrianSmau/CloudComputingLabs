package com.homework.errors;

public class MethodNotAllowedException extends ApplicationException {
    MethodNotAllowedException(int code, String message) {
        super(code, message);
    }
}
