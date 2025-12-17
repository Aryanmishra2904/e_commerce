package com.aryan.e_commerce.order;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private String productId;

    private String sku;

    private String productName;
    private String productImage;

    private String color;

    private Double lengthInMeters;

    private Integer quantity;

    private Double unitPrice; // price at time of purchase
}
