package com.aryan.e_commerce.auth;

import com.aryan.e_commerce.email.SendGridEmailService;
import com.aryan.e_commerce.security.JwtService;
import com.aryan.e_commerce.security.TokenBlacklist;
import com.aryan.e_commerce.security.TokenBlacklistRepository;
import com.aryan.e_commerce.user.Role;
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
    private final TokenBlacklistRepository blacklistRepo;

    // ---------------------------
    //          SIGNUP
    // ---------------------------
    public AuthResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists");

        if (userRepository.existsByPhone(request.getPhone()))
            throw new RuntimeException("Phone number already exists");

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.USER) // ⭐ Default role
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getRole());
    }

    // ---------------------------
    //           LOGIN
    // ---------------------------
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid email or password");

        String token = jwtService.generateToken(user.getId(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getRole());
    }

    // ---------------------------
    //     EMAIL FORGOT PASSWORD
    // ---------------------------
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return;

        String token = Base64.getUrlEncoder().encodeToString(
                UUID.randomUUID().toString().getBytes()
        );

        Instant expiry = Instant.now().plusSeconds(15 * 60);

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(expiry);
        userRepository.save(user);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        String html = """
                <h2>Password Reset Request</h2>
                <p>Click the link below:</p>
                <a href="%s">Reset Password</a>
                """.formatted(resetLink);

        emailService.sendHtmlMail(user.getEmail(), "Reset Password", html);
    }

    // ---------------------------
    //      RESET PASSWORD
    // ---------------------------
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (user.getResetPasswordExpiry().isBefore(Instant.now()))
            throw new RuntimeException("Token expired");

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);

        userRepository.save(user);
    }

    // ---------------------------
    //           LOGOUT
    // ---------------------------
    public void logout(String token) {

        Instant expiry = jwtService.extractExpiration(token);

        TokenBlacklist entry = TokenBlacklist.builder()
                .token(token)
                .expiresAt(expiry)
                .build();

        blacklistRepo.save(entry);
    }
}
