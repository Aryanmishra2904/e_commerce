package com.aryan.e_commerce.product;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String category;

    private Double price;

    private Double discount;

    private int stock;               // ✅ REQUIRED

    private Map<String, String> specifications;

    private List<String> availableColors;

    private Boolean isActive;

    private Instant createdAt;
}
