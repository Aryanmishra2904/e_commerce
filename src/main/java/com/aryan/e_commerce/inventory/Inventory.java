package com.aryan.e_commerce.inventory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory")

public class Inventory {
    @Id
    private String id;

    @Indexed
    private String productId;   // reference to Product

    @Indexed(unique = true)
    private String sku;         // e.g. "BANARASI-RED-6M"

    private String color;       // Saree color

    private Double lengthInMeters; // e.g. 5.5, 6.0 (standard saree length)

    private Integer quantity;
    // stock left
    private String fabricType;  // Silk, Cotton, Georgette (optional)

    private long updatedAt;
}
