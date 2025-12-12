package com.aryan.e_commerce.product.dto;


import lombok.Data;


import java.util.List;
import java.util.Map;


@Data
public class ProductDto {
    private String title;
    private String description;
    private String brand;
    private String category;
    private Double price;
    private Double discount;
    private Map<String, String> specifications;
    private List<String> availableColors;
    private boolean isActive = true;
}