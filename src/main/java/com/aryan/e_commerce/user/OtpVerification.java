package com.aryan.e_commerce.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "otp_verification")
public class OtpVerification {

    @Id
    private String id;

    private String phone;
    private String otp;
    private LocalDateTime expiresAt;
}
