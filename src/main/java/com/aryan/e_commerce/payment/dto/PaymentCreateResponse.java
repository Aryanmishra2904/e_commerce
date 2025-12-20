package com.aryan.e_commerce.payment.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateResponse {

    private String razorpayOrderId;
    private Integer amount;
    private String currency;
}
