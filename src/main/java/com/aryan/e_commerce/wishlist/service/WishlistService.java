package com.aryan.e_commerce.wishlist.service;

import com.aryan.e_commerce.product.ProductRepository;
import com.aryan.e_commerce.wishlist.Wishlist;
import com.aryan.e_commerce.wishlist.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepo;
    private final ProductRepository productRepo;

    public Wishlist addToWishlist(String userId, String productId) {

        productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUserId(userId);
                    return w;
                });

        wishlist.getProductIds().add(productId);
        return wishlistRepo.save(wishlist);
    }

    public Wishlist getWishlist(String userId) {
        return wishlistRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist empty"));
    }
    public Wishlist removeFromWishlist(String userId, String productId) {

        Wishlist wishlist = wishlistRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        wishlist.getProductIds().remove(productId);
        return wishlistRepo.save(wishlist);
    }

}

