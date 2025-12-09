package com.aryan.e_commerce.auth;

import com.aryan.e_commerce.dto.SendOtpRequest;
import com.aryan.e_commerce.dto.VerifyOtpRequest;
import com.aryan.e_commerce.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody SendOtpRequest request) {
        return otpService.sendOtp(request.getPhone());
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequest request) {
        return otpService.verifyOtp(
                request.getPhone(),
                request.getOtp(),
                request.getNewPassword()
        );
    }
}
