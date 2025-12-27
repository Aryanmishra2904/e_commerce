package com.aryan.e_commerce.cart.dto;

import lombok.Data;

@Data
public class UpdateCartQuantityRequest {

    private String productId;
    private String sku;
    private String color;
    private Double lengthInMeters;

    private Integer quantity;
}
