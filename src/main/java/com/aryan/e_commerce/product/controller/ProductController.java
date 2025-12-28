package com.aryan.e_commerce.product.controller;

import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        return productService.getAllProducts(page, size, sort);
    }
    @GetMapping("/search")
    public Page<Product> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        return productService.searchProducts(q, page, size, sort);
    }
}
