package com.aryan.e_commerce.inventory;

import com.aryan.e_commerce.order.Order;
import com.aryan.e_commerce.order.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

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

    // ✅ Deduct stock after payment success
    public void deductStock(Order order) {

        for (OrderItem item : order.getItems()) {

            Inventory inventory = inventoryRepository
                    .findBySkuAndColorAndLengthInMeters(
                            item.getSku(),
                            item.getColor(),
                            item.getLengthInMeters()
                    )
                    .orElseThrow();

            inventory.setAvailableStock(
                    inventory.getAvailableStock() - item.getQuantity()
            );

            if (inventory.getAvailableStock() <= 0) {
                inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
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

            inventoryRepository.save(inventory);
        }
    }
}
