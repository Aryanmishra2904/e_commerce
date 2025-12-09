package com.aryan.e_commerce.payment;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private String orderId;       // Which order this payment belongs to

    private String paymentId;     // Razorpay/Stripe payment ID

    private String gatewayOrderId; // Razorpay/Stripe orderId (important!)

    private String method;         // UPI, CARD, NETBANKING, COD

    private Double amount;

    private String status;        // PENDING, SUCCESS, FAILED, REFUNDED

    private long createdAt;

}
