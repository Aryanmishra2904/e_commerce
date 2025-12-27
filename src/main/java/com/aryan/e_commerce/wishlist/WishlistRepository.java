package com.aryan.e_commerce.wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishlistRepository
        extends MongoRepository<Wishlist, String> {

    Optional<Wishlist> findByUserId(String userId);
}
