package com.aryan.e_commerce.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @GetMapping
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @PatchMapping("/{id}/stock")
    public Inventory updateStock(
            @PathVariable String id,
            @RequestParam Integer stock
    ) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setAvailableStock(stock);
        inventory.setStatus(stock > 0
                ? InventoryStatus.IN_STOCK
                : InventoryStatus.OUT_OF_STOCK);

        return inventoryRepository.save(inventory);
    }
    @GetMapping("/low-stock")
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findByStatus(InventoryStatus.LOW_STOCK);
    }

}
