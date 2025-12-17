package com.aryan.e_commerce.refund;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "return_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {

    @Id
    private String id;

    private String orderId;
    private String userId;

    private String reason;
    private String comment;

    private RefundStatus refundStatus;

    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}
