package com.aryan.e_commerce.auth;

import com.aryan.e_commerce.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String userId;
    private Role role;
}
