package com.aryan.e_commerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Enables @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for stateless APIs
                .csrf(AbstractHttpConfigurer::disable)

                // Authorization Rules
                .authorizeHttpRequests(auth -> auth
                        // ============================
                        // PUBLIC AUTH ROUTES
                        // ============================
                        .requestMatchers(
                                "/auth/signup",
                                "/auth/login",
                                "/auth/logout",
                                "/auth/forgot-password",
                                "/auth/reset-password",
                                "/auth/send-otp",
                                "/auth/verify-otp"
                        ).permitAll()

                        // ============================
                        // IMAGEKIT (PUBLIC AUTH TOKEN)
                        // ============================
                        .requestMatchers("/imagekit/auth").permitAll()

                        // ============================
                        // IMAGE UPLOAD (Admin only)
                        // ============================
                        .requestMatchers("/imagekit/upload").hasRole("ADMIN")

                        // ============================
                        // ADMIN MODULE
                        // ============================
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // ============================
                        // EVERYTHING ELSE REQUIRES AUTH
                        // ============================
                        .anyRequest().authenticated()
                )

                // Stateless Session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Disable login forms
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        // Add JWT Filter BEFORE Username/Password auth
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
