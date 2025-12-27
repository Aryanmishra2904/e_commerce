package com.aryan.e_commerce.cart.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private String productId;
    private int quantity;
}

