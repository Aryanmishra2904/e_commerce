package com.aryan.e_commerce.cart;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    @Indexed
    private String userId;  // each user has one cart

    private List<CartItem> items;

    private Double total;   // auto-calculated

    private long updatedAt;
}
