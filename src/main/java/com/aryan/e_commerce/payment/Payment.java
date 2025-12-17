package com.aryan.e_commerce.payment;

import com.aryan.e_commerce.order.PaymentMode;
import com.aryan.e_commerce.order.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    private String id;

    private String orderId;

    private PaymentMode mode;
    private PaymentStatus status;

    private String gatewayPaymentId;
    private String gatewayOrderId;

    private double amount;

    private LocalDateTime createdAt;
}
