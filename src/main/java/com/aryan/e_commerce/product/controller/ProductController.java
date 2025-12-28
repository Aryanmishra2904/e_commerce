package com.aryan.e_commerce.product.controller;

import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 🔓 Browse products (Redis cached)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 🔓 View specific product (Redis cached)
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProduct(id);
    }

    // 🔍 Search products (Redis + MongoDB text search)
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String q) {
        return productService.searchProducts(q);
    }
}
