package com.aryan.e_commerce.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LowStockAlertService {

    private final LowStockAlertRepository alertRepository;

    public void triggerAlert(Inventory inventory) {

        LowStockAlert alert = LowStockAlert.builder()
                .inventoryId(inventory.getId())
                .sku(inventory.getSku())
                .color(inventory.getColor())
                .lengthInMeters(inventory.getLengthInMeters())
                .currentStock(inventory.getAvailableStock())
                .triggeredAt(LocalDateTime.now())
                .build();

        alertRepository.save(alert);

        // Later: email / Slack / SMS
        System.out.println("⚠ LOW STOCK ALERT for SKU: " + inventory.getSku());
    }
}

