package com.aryan.e_commerce.cart.service;

import com.aryan.e_commerce.cart.Cart;
import com.aryan.e_commerce.cart.CartItem;
import com.aryan.e_commerce.cart.CartRepository;
import com.aryan.e_commerce.cart.dto.AddToCartRequest;
import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;


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
                .filter(item ->
                        item.getProductId().equals(request.getProductId()) &&
                                item.getSku().equals(request.getSku()) &&
                                item.getColor().equals(request.getColor())
                )
                .findFirst();

        if (existingItem.isPresent()) {

            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + request.getQuantity()
            );

        } else {

            CartItem newItem = CartItem.builder()
                    .productId(request.getProductId())
                    .sku(request.getSku())
                    .color(request.getColor())
                    .lengthInMeters(request.getLengthInMeters())
                    .quantity(request.getQuantity())
                    .priceAtThatTime(product.getPrice()) // snapshot price
                    .build();

            cart.getItems().add(newItem);
        }

        return cartRepo.save(cart);
    }

    public Cart getCart(String userId) {
        return cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
    }
    public Cart removeFromCart(String userId, String productId) {

        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(
                item -> item.getProductId().equals(productId)
        );

        return cartRepo.save(cart);
    }

}
