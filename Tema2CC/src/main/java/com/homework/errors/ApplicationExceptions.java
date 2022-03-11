package com.homework.errors;

import java.util.function.Function;
import java.util.function.Supplier;

public class ApplicationExceptions {
    public static Function<? super Throwable, RuntimeException> badRequest() {
        return thr -> new BadRequestException(400, thr.getMessage());
    }

    public static Supplier<RuntimeException> methodNotAllowed(String message) {
        return () -> new MethodNotAllowedException(405, message);
    }

    public static Supplier<RuntimeException> conflict(String message) {
        return () -> new ConflictException(409, message);
    }

    public static Supplier<RuntimeException> notFound(String message) {
        return () -> new NotFoundException(404, message);
    }
}
