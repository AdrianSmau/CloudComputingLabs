package com.homework.entities.DTOs;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDTO {
    String username;
    String password;
}
