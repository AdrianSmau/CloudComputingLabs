package com.homework.api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
