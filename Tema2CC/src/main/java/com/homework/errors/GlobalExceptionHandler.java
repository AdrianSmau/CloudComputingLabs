package com.homework.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.Constants;
import com.homework.entities.DTOs.ErrorDTO;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handle(Throwable throwable, HttpExchange exchange) {
        try {
            throwable.printStackTrace();
            exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            ErrorDTO response = getErrorResponse(throwable, exchange);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(objectMapper.writeValueAsBytes(response));
            responseBody.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ErrorDTO getErrorResponse(Throwable throwable, HttpExchange exchange) throws IOException {
        ErrorDTO.ErrorDTOBuilder responseBuilder = ErrorDTO.builder();
        if (throwable instanceof BadRequestException) {
            BadRequestException exc = (BadRequestException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(400, 0);
        } else if (throwable instanceof ConflictException) {
            ConflictException exc = (ConflictException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(409, 0);
        } else if (throwable instanceof NotFoundException) {
            NotFoundException exc = (NotFoundException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(404, 0);
        } else if (throwable instanceof MethodNotAllowedException) {
            MethodNotAllowedException exc = (MethodNotAllowedException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(405, 0);
        } else {
            responseBuilder.code(500).message(throwable.getMessage());
            exchange.sendResponseHeaders(500, 0);
        }
        return responseBuilder.build();
    }

}
