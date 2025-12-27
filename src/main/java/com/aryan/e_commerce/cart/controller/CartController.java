package com.aryan.e_commerce.cart.controller;

import com.aryan.e_commerce.cart.Cart;
import com.aryan.e_commerce.cart.dto.AddToCartRequest;
import com.aryan.e_commerce.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public Cart addToCart(
            @RequestBody AddToCartRequest request,
            Authentication authentication) {

        String userId = authentication.getName();
        return cartService.addToCart(userId, request);
    }

    @GetMapping
    public Cart viewCart(Authentication authentication) {
        return cartService.getCart(authentication.getName());
    }
}
