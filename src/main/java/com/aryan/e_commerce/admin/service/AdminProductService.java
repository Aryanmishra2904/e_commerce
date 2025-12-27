package com.aryan.e_commerce.admin.service;

import com.aryan.e_commerce.imagekit.ImageKitService;
import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.ProductRepository;
import com.aryan.e_commerce.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepo;
    private final ImageKitService imageKitService;
    public Product createProduct(ProductDto dto, List<MultipartFile> images) {

        System.out.println("➡️ Service: createProduct() STARTED");

        List<String> uploaded = new ArrayList<>();

        if (images == null) {
            System.out.println("⚠️ images list is NULL");
        } else if (images.isEmpty()) {
            System.out.println("⚠️ images list is EMPTY");
        } else {
            System.out.println("🖼️ Total images received: " + images.size());

            for (int i = 0; i < images.size(); i++) {
                MultipartFile img = images.get(i);

                System.out.println("⬆️ Uploading image #" + (i + 1));
                System.out.println("   Name: " + img.getOriginalFilename());
                System.out.println("   Size: " + img.getSize());
                System.out.println("   Type: " + img.getContentType());

                String url = imageKitService.upload(img);

                if (url != null) {
                    System.out.println("✅ Image uploaded successfully");
                    System.out.println("   URL: " + url);
                    uploaded.add(url);
                } else {
                    System.out.println("❌ Image upload FAILED for: " + img.getOriginalFilename());
                }
            }
        }

        System.out.println("📦 Total uploaded image URLs: " + uploaded.size());

        System.out.println("🛠️ Building Product object");

        Product p = Product.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .brand(dto.getBrand())
                .discount(dto.getDiscount())
                .price(dto.getPrice())
                .specifications(dto.getSpecifications())
                .availableColors(dto.getAvailableColors())
                .images(uploaded)
                .isActive(dto.isActive())
                .createdAt(Instant.ofEpochSecond(Instant.now().toEpochMilli()))
                .build();

        System.out.println("💾 Saving product to database");

        Product saved = productRepo.save(p);

        System.out.println("✅ Product saved successfully");
        System.out.println("🆔 Product ID: " + saved.getId());

        System.out.println("➡️ Service: createProduct() FINISHED\n");

        return saved;
    }
    public Optional<Product> updateProduct(String id, ProductDto dto, List<MultipartFile> images) {

        System.out.println("➡️ Service: updateProduct() STARTED");
        System.out.println("🆔 Product ID: " + id);

        return productRepo.findById(id).map(existing -> {

            System.out.println("✅ Existing product found");

            existing.setTitle(dto.getTitle());
            existing.setDescription(dto.getDescription());
            existing.setBrand(dto.getBrand());
            existing.setCategory(dto.getCategory());
            existing.setDiscount(dto.getDiscount());
            existing.setPrice(dto.getPrice());
            existing.setSpecifications(dto.getSpecifications());
            existing.setAvailableColors(dto.getAvailableColors());
            existing.setActive(dto.isActive());

            if (images == null || images.isEmpty()) {
                System.out.println("⚠️ No new images provided for update");
            } else {
                System.out.println("🖼️ New images received: " + images.size());

                List<String> newImages = new ArrayList<>();

                for (MultipartFile img : images) {
                    System.out.println("⬆️ Uploading new image: " + img.getOriginalFilename());

                    String url = imageKitService.upload(img);

                    if (url != null) {
                        System.out.println("✅ Image uploaded: " + url);
                        newImages.add(url);
                    } else {
                        System.out.println("❌ Image upload failed: " + img.getOriginalFilename());
                    }
                }

                existing.setImages(newImages);
            }

            System.out.println("💾 Saving updated product");

            Product updated = productRepo.save(existing);

            System.out.println("✅ Product updated successfully");
            return updated;
        });
    }
    public void deleteProduct(String id) {
        System.out.println("🗑️ Deleting product with ID: " + id);
        productRepo.deleteById(id);
        System.out.println("✅ Product deleted");
    }

    public Product getProduct(String id) {
        System.out.println("🔍 Fetching product with ID: " + id);
        return productRepo.findById(id).orElse(null);
    }

    public List<Product> listProducts() {
        System.out.println("📄 Fetching all products");
        return productRepo.findAll();
    }
}