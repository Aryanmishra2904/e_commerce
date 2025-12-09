package com.aryan.e_commerce.wishlist;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlists")
public class Wishlist {

    @Id
    private String id;

    @Indexed
    private String userId;  // FAST lookup of user's wishlist

    private List<String> productIds;  // store product IDs only

    private long createdAt;
}
