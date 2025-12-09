package com.aryan.e_commerce.service;

import com.aryan.e_commerce.user.OtpVerification;
import com.aryan.e_commerce.user.OtpVerificationRepository;
import com.aryan.e_commerce.user.User;
import com.aryan.e_commerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpVerificationRepository otpRepo;
    private final UserRepository userRepo;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;

    // 1️⃣ SEND OTP
    public String sendOtp(String phone) {

        // ⭐ FIX: Validate phone input
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("Phone number cannot be empty");
        }

        phone = phone.trim();

        // ⭐ FIX: Avoid querying with null and return clean error
        Optional<User> user = userRepo.findByPhone(phone);
        if (user.isEmpty()) {
            throw new RuntimeException("Phone number is not registered");
        }

        // Generate 4-digit OTP
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);

        // Delete previous OTP for this phone if exists
        otpRepo.findByPhone(phone).ifPresent(otpRepo::delete);

        // Create OTP entry
        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setPhone(phone);
        otpEntity.setOtp(otp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        otpRepo.save(otpEntity);

        // Send OTP via MSG91
        smsService.sendSms(phone, otp);

        return "OTP sent successfully to " + phone;
    }

    // 2️⃣ VERIFY OTP & RESET PASSWORD
    public String verifyOtp(String phone, String otp, String newPassword) {

        // ⭐ FIX: Validate phone & otp input
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("Phone number cannot be empty");
        }
        if (otp == null || otp.trim().isEmpty()) {
            throw new RuntimeException("OTP cannot be empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new RuntimeException("New password cannot be empty");
        }

        phone = phone.trim();
        otp = otp.trim();

        // Get OTP document
        OtpVerification otpEntity = otpRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("OTP not found for this phone"));

        // Check expiration
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        // Check OTP match
        if (!otpEntity.getOtp().equals(otp)) {
            throw new RuntimeException("Incorrect OTP");
        }

        // Get user
        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        // Delete OTP after success
        otpRepo.delete(otpEntity);

        return "Password reset successful!";
    }
}
