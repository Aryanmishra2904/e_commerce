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
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService service;
    private final ObjectMapper objectMapper;

    // =============================
    // CREATE PRODUCT (JSON + IMAGES)
    // =============================
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {

        System.out.println("🚀 CREATE PRODUCT API HIT");

        // 1️⃣ Log raw product JSON
        System.out.println("📦 Product JSON received:");
        System.out.println(productJson);

        // 2️⃣ Parse DTO
        ProductDto dto = objectMapper.readValue(productJson, ProductDto.class);
        System.out.println("✅ Product DTO parsed successfully");

        // 3️⃣ Check images
        if (images == null || images.isEmpty()) {
            System.out.println("⚠️ No images received in request");
        } else {
            System.out.println("🖼️ Total images received: " + images.size());
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                System.out.println("➡️ Image " + (i + 1));
                System.out.println("   Name: " + file.getOriginalFilename());
                System.out.println("   Size: " + file.getSize());
                System.out.println("   Type: " + file.getContentType());
            }
        }

        // 4️⃣ Call service
        System.out.println("📞 Calling AdminProductService.createProduct()");
        Product saved = service.createProduct(dto, images);

        System.out.println("✅ Product created successfully with ID: " + saved.getId());

        return ResponseEntity.ok(saved);
    }

    // =============================
    // UPDATE PRODUCT
    // =============================
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {

        System.out.println("✏️ UPDATE PRODUCT API HIT");
        System.out.println("🆔 Product ID: " + id);

        System.out.println("📦 Product JSON received:");
        System.out.println(productJson);

        ProductDto dto = objectMapper.readValue(productJson, ProductDto.class);
        System.out.println("✅ Product DTO parsed");

        if (images == null || images.isEmpty()) {
            System.out.println("⚠️ No images received for update");
        } else {
            System.out.println("🖼️ Images received for update: " + images.size());
        }

        System.out.println("📞 Calling AdminProductService.updateProduct()");

        return service.updateProduct(id, dto, images)
                .map(product -> {
                    System.out.println("✅ Product updated successfully");
                    return ResponseEntity.ok(product);
                })
                .orElseGet(() -> {
                    System.out.println("❌ Product not found for ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    // =============================
    // GET ALL PRODUCTS
    // =============================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> getAllProducts() {

        System.out.println("📄 GET ALL PRODUCTS API HIT");
        List<Product> products = service.listProducts();
        System.out.println("📊 Total products found: " + products.size());

        return ResponseEntity.ok(products);
    }

    // =============================
    // GET SINGLE PRODUCT
    // =============================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {

        System.out.println("🔍 GET PRODUCT API HIT");
        System.out.println("🆔 Product ID: " + id);

        Product product = service.getProduct(id);

        if (product == null) {
            System.out.println("❌ Product not found");
            return ResponseEntity.notFound().build();
        }

        System.out.println("✅ Product found");
        return ResponseEntity.ok(product);
    }

    // =============================
    // DELETE PRODUCT
    // =============================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {

        System.out.println("🗑️ DELETE PRODUCT API HIT");
        System.out.println("🆔 Product ID: " + id);

        service.deleteProduct(id);
        System.out.println("✅ Product deleted");

        return ResponseEntity.ok().build();
    }
}
