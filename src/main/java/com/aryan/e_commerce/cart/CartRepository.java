package com.aryan.e_commerce.cart;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.ScopedValue;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}
