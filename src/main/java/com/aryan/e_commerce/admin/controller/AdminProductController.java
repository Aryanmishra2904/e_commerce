package com.aryan.e_commerce.admin.controller;

import com.aryan.e_commerce.admin.service.AdminProductService;
import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductService service;
    private final ObjectMapper objectMapper;

    // =============================
    // CREATE PRODUCT
    // =============================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> create(
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {

        ProductDto dto = objectMapper.readValue(productJson, ProductDto.class);
        return ResponseEntity.ok(service.createProduct(dto, images));
    }

    // =============================
    // UPDATE PRODUCT
    // =============================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(
            @PathVariable String id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {

        ProductDto dto = objectMapper.readValue(productJson, ProductDto.class);

        return service.updateProduct(id, dto, images)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =============================
    // READ
    // =============================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(service.listProducts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> get(@PathVariable String id) {
        Product p = service.getProduct(id);
        return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }

    // =============================
    // DELETE
    // =============================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
