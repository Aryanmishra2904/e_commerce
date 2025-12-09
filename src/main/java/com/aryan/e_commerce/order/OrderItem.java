package com.aryan.e_commerce.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private String productId;

    private String sku;

    private String color;

    private Double lengthInMeters;

    private Integer quantity;

    private Double price;  // price at the moment of purchase
}
