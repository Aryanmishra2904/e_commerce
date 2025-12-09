package com.aryan.e_commerce.order;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed
    private String userId;

    private List<OrderItem> items;

    private Double totalAmount;

    @Indexed
    private String paymentStatus;

    @Indexed
    private String orderStatus;

    private long createdAt;

    private OrderAddress shippingAddress;
}
