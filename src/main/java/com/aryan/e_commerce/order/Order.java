package com.aryan.e_commerce.order;

import com.aryan.e_commerce.order.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;

    private String userId;

    private List<OrderItem> items;

    private double totalAmount;

    private OrderStatus status;

    // 🔑 Payment snapshot for admin
    private PaymentStatus paymentStatus;
    private PaymentMode paymentMode;
    private String paymentId;

    private OrderAddress shippingAddress;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
