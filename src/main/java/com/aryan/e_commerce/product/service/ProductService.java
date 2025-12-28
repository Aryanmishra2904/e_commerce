package com.aryan.e_commerce.product.service;

import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 🔹 Browse all products (cached)
    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 🔹 View single product (cached)
    @Cacheable(value = "product", key = "#id")
    public Product getProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // 🔍 SEARCH PRODUCTS (Redis + MongoDB text search)
    @Cacheable(
            value = "product-search",
            key = "#keyword.toLowerCase()"
    )
    public List<Product> searchProducts(String keyword) {

        TextCriteria criteria = TextCriteria
                .forDefaultLanguage()
                .matchingAny(keyword);

        return productRepository.findBy(criteria);
    }

    // 🔥 Evict ALL related caches when product changes
    @CacheEvict(
            value = {"products", "product", "product-search"},
            allEntries = true
    )
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
