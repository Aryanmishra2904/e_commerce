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
    private String userId;
    private Double amount;
    private PaymentStatus status;
    private PaymentMode mode;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String payerName;
    private String payerEmail;
    private String payerContact;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    public void setMethod(String method) {
    }
}
