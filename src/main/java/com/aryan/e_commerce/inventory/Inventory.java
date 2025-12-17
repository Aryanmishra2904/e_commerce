package com.aryan.e_commerce.inventory;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    private String id;

    private String productId;

    private String sku;

    private String color;

    private Double lengthInMeters;

    private Integer availableStock;

    private Integer reservedStock; // optional (future)

    private InventoryStatus status; // IN_STOCK / OUT_OF_STOCK
}
