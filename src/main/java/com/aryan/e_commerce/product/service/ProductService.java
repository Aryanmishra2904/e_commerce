package com.aryan.e_commerce.product.service;

import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    @Cacheable(
            value = "products-page",
            key = "#page + ':' + #size + ':' + #sort"
    )
    public Page<Product> getAllProducts(
            int page,
            int size,
            String sort
    ) {

        Pageable pageable = buildPageable(page, size, sort);
        return productRepository.findAll(pageable);
    }
    @Cacheable(
            value = "product-search-page",
            key = "#keyword.toLowerCase() + ':' + #page + ':' + #size + ':' + #sort"
    )
    public Page<Product> searchProducts(
            String keyword,
            int page,
            int size,
            String sort
    ) {

        Pageable pageable = buildPageable(page, size, sort);

        TextCriteria criteria = TextCriteria
                .forDefaultLanguage()
                .matchingAny(keyword);

        return productRepository.findBy(criteria, pageable);
    }
    @CacheEvict(
            value = {
                    "products",
                    "product",
                    "product-search",
                    "products-page",
                    "product-search-page"
            },
            allEntries = true
    )
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    private Pageable buildPageable(int page, int size, String sort) {

        if (sort == null || sort.isBlank()) {
            return PageRequest.of(page, size);
        }

        String[] sortParams = sort.split(",");
        Sort.Direction direction =
                sortParams[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortParams[0])
        );
    }
}
