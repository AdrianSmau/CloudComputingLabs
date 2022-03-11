package com.homework.entities.DTOs;

import com.homework.entities.StatusCodes;
import com.sun.net.httpserver.Headers;
import lombok.Value;

@Value
public class ResponseDTO<T> {
    T body;
    Headers headers;
    StatusCodes statusCode;
}
