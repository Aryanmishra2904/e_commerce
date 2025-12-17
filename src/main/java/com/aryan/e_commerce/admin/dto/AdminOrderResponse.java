package com.aryan.e_commerce.admin.dto;

import com.aryan.e_commerce.order.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminOrderResponse {

    private String orderId;
    private String userId;

    private List<OrderItem> items;

    private double totalAmount;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;
    private PaymentMode paymentMode;
    private String transactionId;

    private OrderAddress shippingAddress;

    private LocalDateTime orderedAt;
}
