package com.aryan.e_commerce.inventory;

import com.aryan.e_commerce.order.Order;
import com.aryan.e_commerce.order.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final LowStockAlertService lowStockAlertService;

    // ✅ Check stock before checkout
    public void validateStock(Order order) {

        for (OrderItem item : order.getItems()) {

            Inventory inventory = inventoryRepository
                    .findBySkuAndColorAndLengthInMeters(
                            item.getSku(),
                            item.getColor(),
                            item.getLengthInMeters()
                    )
                    .orElseThrow(() ->
                            new RuntimeException("Inventory not found for SKU: " + item.getSku())
                    );

            if (inventory.getAvailableStock() < item.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for SKU: " + item.getSku()
                );
            }
        }
    }

    // ✅ Deduct stock after payment success + LOW STOCK ALERT
    public void deductStock(Order order) {

        for (OrderItem item : order.getItems()) {

            Inventory inventory = inventoryRepository
                    .findBySkuAndColorAndLengthInMeters(
                            item.getSku(),
                            item.getColor(),
                            item.getLengthInMeters()
                    )
                    .orElseThrow();

            int remainingStock =
                    inventory.getAvailableStock() - item.getQuantity();

            inventory.setAvailableStock(remainingStock);

            // 🔑 STATUS UPDATE
            if (remainingStock <= 0) {
                inventory.setStatus(InventoryStatus.OUT_OF_STOCK);

            } else if (remainingStock <= inventory.getLowStockThreshold()) {
                inventory.setStatus(InventoryStatus.LOW_STOCK);

                // 🔔 Trigger alert only once
                if (!inventory.isLowStockAlertSent()) {
                    lowStockAlertService.triggerAlert(inventory);
                    inventory.setLowStockAlertSent(true);
                }

            } else {
                inventory.setStatus(InventoryStatus.IN_STOCK);
            }

            inventoryRepository.save(inventory);
        }
    }

    // ✅ Restore stock on cancel / return
    public void restoreStock(Order order) {

        for (OrderItem item : order.getItems()) {

            Inventory inventory = inventoryRepository
                    .findBySkuAndColorAndLengthInMeters(
                            item.getSku(),
                            item.getColor(),
                            item.getLengthInMeters()
                    )
                    .orElseThrow();

            inventory.setAvailableStock(
                    inventory.getAvailableStock() + item.getQuantity()
            );

            inventory.setStatus(InventoryStatus.IN_STOCK);
            inventory.setLowStockAlertSent(false); // reset alert

            inventoryRepository.save(inventory);
        }
    }
}
