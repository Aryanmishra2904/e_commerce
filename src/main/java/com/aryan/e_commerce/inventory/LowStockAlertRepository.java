package com.aryan.e_commerce.inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface LowStockAlertRepository
        extends MongoRepository<LowStockAlert, String> {
    Optional<LowStockAlert> findByInventoryId(String inventoryId);
    List<LowStockAlert> findAllByOrderByTriggeredAtDesc();
    List<LowStockAlert> findBySku(String sku);
}
