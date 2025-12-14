package com.aryan.e_commerce.admin.controller;

import com.aryan.e_commerce.admin.service.AdminProductService;
import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log =
            LoggerFactory.getLogger(AdminProductController.class);

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

        log.info("🚀 CREATE PRODUCT API HIT");

        log.debug("📦 Raw product JSON: {}", productJson);

        ProductDto dto = objectMapper.readValue(productJson, ProductDto.class);
        log.info("✅ Product DTO parsed successfully");

        if (images == null || images.isEmpty()) {
            log.warn("⚠️ No images received in request");
        } else {
            log.info("🖼️ Total images received: {}", images.size());
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                log.info(
                        "➡️ Image {} | Name={} | Size={} | Type={}",
                        i + 1,
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType()
                );
            }
        }

        log.info("📞 Calling AdminProductService.createProduct()");
        Product saved = service.createProduct(dto, images);

        log.info("✅ Product created successfully | ID={}", saved.getId());

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

        log.info("✏️ UPDATE PRODUCT API HIT | productId={}", id);
        log.debug("📦 Raw product JSON: {}", productJson);

        ProductDto dto = objectMapper.readValue(productJson, ProductDto.class);
        log.info("✅ Product DTO parsed successfully");

        if (images == null || images.isEmpty()) {
            log.warn("⚠️ No images received for update");
        } else {
            log.info("🖼️ Images received for update: {}", images.size());
        }

        log.info("📞 Calling AdminProductService.updateProduct()");

        return service.updateProduct(id, dto, images)
                .map(product -> {
                    log.info("✅ Product updated successfully | ID={}", id);
                    return ResponseEntity.ok(product);
                })
                .orElseGet(() -> {
                    log.error("❌ Product not found | ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // =============================
    // GET ALL PRODUCTS
    // =============================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> getAllProducts() {

        log.info("📄 GET ALL PRODUCTS API HIT");

        List<Product> products = service.listProducts();
        log.info("📊 Total products found: {}", products.size());

        return ResponseEntity.ok(products);
    }

    // =============================
    // GET SINGLE PRODUCT
    // =============================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {

        log.info("🔍 GET PRODUCT API HIT | ID={}", id);

        Product product = service.getProduct(id);

        if (product == null) {
            log.warn("❌ Product not found | ID={}", id);
            return ResponseEntity.notFound().build();
        }

        log.info("✅ Product found | ID={}", id);
        return ResponseEntity.ok(product);
    }

    // =============================
    // DELETE PRODUCT
    // =============================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {

        log.info("🗑️ DELETE PRODUCT API HIT | ID={}", id);

        service.deleteProduct(id);

        log.info("✅ Product deleted | ID={}", id);
        return ResponseEntity.ok().build();
    }
}
