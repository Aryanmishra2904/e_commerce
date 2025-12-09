package com.aryan.e_commerce.product;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    private List<String> images;

    @TextIndexed
    private String brand;

    @TextIndexed
    private String category;   // Silk Saree, Banarasi Saree, Soft Silk

    private Double price;

    private Double discount;

    private Map<String, String> specifications;
    // e.g { "fabric": "Silk", "pattern": "Banarasi" }

    private List<String> availableColors;

    private boolean isActive;

    private long createdAt;
}
