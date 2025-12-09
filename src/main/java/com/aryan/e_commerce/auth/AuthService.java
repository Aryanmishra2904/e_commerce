package com.aryan.e_commerce.auth;

import com.aryan.e_commerce.email.SendGridEmailService;
import com.aryan.e_commerce.security.JwtService;
import com.aryan.e_commerce.user.User;
import com.aryan.e_commerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SendGridEmailService emailService;

    // =============================
    //           SIGNUP
    // =============================
    public AuthResponse signup(SignupRequest request) {

        // Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check duplicate phone (important)
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())   // ⭐ ADDED
                .role("USER")
                .build();

        userRepository.save(user);

        // Generate token
        String token = jwtService.generateToken(user.getId(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getRole());
    }

    // =============================
    //            LOGIN
    // =============================
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getRole());
    }

    // =============================
    //       FORGOT PASSWORD (EMAIL)
    // =============================
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        // DO NOT reveal whether email exists — security best practice
        if (user == null) {
            return;
        }

        // Generate secure random token
        String token = Base64.getUrlEncoder().encodeToString(
                UUID.randomUUID().toString().getBytes()
        );

        // Expiry: 15 minutes
        Instant expiry = Instant.now().plusSeconds(15 * 60);

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(expiry);
        userRepository.save(user);

        // Create reset link
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        // Create email content
        String html = "<h2>Password Reset Request</h2>" +
                "<p>Click below to reset your password:</p>" +
                "<a href=\"" + resetLink + "\">Reset Password</a>" +
                "<p>This link expires in 15 minutes.</p>";

        emailService.sendHtmlMail(user.getEmail(), "Reset Your Password", html);
    }

    // =============================
    //        RESET PASSWORD (EMAIL)
    // =============================
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        // Check expiry
        if (user.getResetPasswordExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }

        // Change password
        user.setPassword(passwordEncoder.encode(newPassword));

        // Clear reset token
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);

        userRepository.save(user);
    }
}
