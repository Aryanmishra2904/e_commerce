package com.aryan.e_commerce.wishlist.controller;

import com.aryan.e_commerce.wishlist.Wishlist;
import com.aryan.e_commerce.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/wishlist")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{productId}")
    public Wishlist addToWishlist(
            @PathVariable String productId,
            Authentication authentication) {

        return wishlistService.addToWishlist(
                authentication.getName(), productId
        );
    }

    @GetMapping
    public Wishlist getWishlist(Authentication authentication) {
        return wishlistService.getWishlist(authentication.getName());
    }
}

