package com.aryan.e_commerce.cart.service;

import com.aryan.e_commerce.cart.Cart;
import com.aryan.e_commerce.cart.CartItem;
import com.aryan.e_commerce.cart.CartRepository;
import com.aryan.e_commerce.cart.dto.AddToCartRequest;
import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public Cart addToCart(String userId, AddToCartRequest request) {

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        Cart cart = cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUserId(userId);
                    return c;
                });

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + request.getQuantity()
            );
        } else {
            cart.getItems().add(
                    new CartItem(request.getProductId(), request.getQuantity())
            );
        }

        return cartRepo.save(cart);
    }

    public Cart getCart(String userId) {
        return cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
    }
}
