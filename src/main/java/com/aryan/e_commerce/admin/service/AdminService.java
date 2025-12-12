package com.aryan.e_commerce.admin.service;

import com.aryan.e_commerce.order.Order;
import com.aryan.e_commerce.order.OrderRepository;
import com.aryan.e_commerce.product.Product;
import com.aryan.e_commerce.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    // ============================
    //        PRODUCT CRUD
    // ============================

    public Product createProduct(Product p) {
        p.setCreatedAt(System.currentTimeMillis()); // timestamp
        p.setActive(true); // default active
        return productRepo.save(p);
    }

    public Optional<Product> updateProduct(String id, Product p) {
        return productRepo.findById(id).map(existing -> {

            existing.setTitle(p.getTitle());
            existing.setDescription(p.getDescription());
            existing.setBrand(p.getBrand());
            existing.setCategory(p.getCategory());
            existing.setPrice(p.getPrice());
            existing.setDiscount(p.getDiscount());
            existing.setSpecifications(p.getSpecifications());
            existing.setAvailableColors(p.getAvailableColors());
            existing.setImages(p.getImages());
            existing.setActive(p.isActive());

            return productRepo.save(existing);
        });
    }

    public void deleteProduct(String id) {
        productRepo.deleteById(id);
    }

    public Product getProduct(String id) {
        return productRepo.findById(id).orElse(null);
    }

    public List<Product> listProducts() {
        return productRepo.findAll();
    }

    // ============================
    //        ORDER MANAGEMENT
    // ============================

    public List<Order> listOrders() {
        return orderRepo.findAll();
    }

    public Optional<Order> updateOrderStatus(String orderId, String status) {
        return orderRepo.findById(orderId).map(order -> {
            order.setOrderStatus(status);  // CORRECT FIELD
            return orderRepo.save(order);
        });
    }

    // ============================
    //       DASHBOARD ANALYTICS
    // ============================

    public long totalProducts() {
        return productRepo.count();
    }

    public long totalOrders() {
        return orderRepo.count();
    }
}
