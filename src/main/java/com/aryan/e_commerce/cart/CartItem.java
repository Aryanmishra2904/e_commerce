package com.aryan.e_commerce.cart;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String productId;

    private String sku;    // matches your inventory record

    private String color;

    private Double lengthInMeters;  // saree length like 5.5, 6.0 m

    private Integer quantity;

    private Double priceAtThatTime;
}
