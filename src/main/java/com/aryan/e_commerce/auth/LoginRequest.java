package com.aryan.e_commerce.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
