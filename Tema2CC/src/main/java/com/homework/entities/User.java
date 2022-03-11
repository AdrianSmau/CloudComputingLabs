package com.homework.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    String id;
    String username;
    String password;
}
