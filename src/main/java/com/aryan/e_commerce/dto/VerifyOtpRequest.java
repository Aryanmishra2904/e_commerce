package com.aryan.e_commerce.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String phone;
    private String otp;
    private String newPassword;
}
