package com.aryan.e_commerce.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "low_stock_alerts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LowStockAlert {

    @Id
    private String id;

    private String inventoryId;

    private String sku;
    private String color;
    private Double lengthInMeters;

    private Integer currentStock;

    private LocalDateTime triggeredAt;
}

