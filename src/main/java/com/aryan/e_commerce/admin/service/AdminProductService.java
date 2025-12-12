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

        List<String> uploaded = new ArrayList<>();
        if (images != null)
            for (MultipartFile img : images)
                uploaded.add(imageKitService.upload(img));

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
                .createdAt(Instant.now().toEpochMilli())
                .build();

        return productRepo.save(p);
    }

    public Optional<Product> updateProduct(String id, ProductDto dto, List<MultipartFile> images) {

        return productRepo.findById(id).map(existing -> {

            existing.setTitle(dto.getTitle());
            existing.setDescription(dto.getDescription());
            existing.setBrand(dto.getBrand());
            existing.setCategory(dto.getCategory());
            existing.setDiscount(dto.getDiscount());
            existing.setPrice(dto.getPrice());
            existing.setSpecifications(dto.getSpecifications());
            existing.setAvailableColors(dto.getAvailableColors());
            existing.setActive(dto.isActive());

            if (images != null && !images.isEmpty()) {
                List<String> newImages = new ArrayList<>();
                for (MultipartFile img : images)
                    newImages.add(imageKitService.upload(img));
                existing.setImages(newImages);
            }

            return productRepo.save(existing);
        });
    }

    public void deleteProduct(String id) { productRepo.deleteById(id); }
    public Product getProduct(String id) { return productRepo.findById(id).orElse(null); }
    public List<Product> listProducts() { return productRepo.findAll(); }
}
