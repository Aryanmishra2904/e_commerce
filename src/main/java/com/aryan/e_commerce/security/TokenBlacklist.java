package com.aryan.e_commerce.security;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "token_blacklist")
public class TokenBlacklist {

    @Id
    private String id;

    private String token;

    private Instant expiresAt;
}
