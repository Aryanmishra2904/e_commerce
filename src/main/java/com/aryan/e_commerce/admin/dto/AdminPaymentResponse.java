package com.aryan.e_commerce.admin.dto;

import com.aryan.e_commerce.order.PaymentMode;
import com.aryan.e_commerce.order.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentResponse {

    private String paymentId;
    private String orderId;
    private String userId;

    private Double amount;

    private PaymentStatus status;
    private PaymentMode paymentMode;

    // Razorpay
    private String transactionId; // razorpayPaymentId
    private String razorpayOrderId;

    // Payer
    private String payerName;
    private String payerEmail;
    private String payerContact;

    // Method info
    private String method;
    private String bank;
    private String wallet;

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}
