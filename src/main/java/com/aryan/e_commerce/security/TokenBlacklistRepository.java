package com.aryan.e_commerce.security;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends MongoRepository<TokenBlacklist, String> {
    Optional<TokenBlacklist> findByToken(String token);
}
