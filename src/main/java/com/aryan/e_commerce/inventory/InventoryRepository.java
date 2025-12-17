package com.aryan.e_commerce.inventory;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InventoryRepository extends MongoRepository<Inventory, String> {

    Optional<Inventory> findBySkuAndColorAndLengthInMeters(
            String sku,
            String color,
            Double lengthInMeters
    );
}
