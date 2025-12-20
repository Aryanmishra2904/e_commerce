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
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF (stateless APIs)
                .csrf(AbstractHttpConfigurer::disable)

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
                        // IMAGEKIT
                        // ============================
                        .requestMatchers("/imagekit/auth").permitAll()
                        .requestMatchers("/imagekit/upload").hasRole("ADMIN")

                        // ============================
                        // PAYMENTS (PUBLIC FOR TESTING)
                        // ============================
                        .requestMatchers(
                                "/payments/create/**",
                                "/payments/webhook"
                        ).permitAll()

                        // ============================
                        // ADMIN MODULE
                        // ============================
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // ============================
                        // EVERYTHING ELSE
                        // ============================
                        .anyRequest().authenticated()
                )

                // Stateless session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Disable form login & basic auth
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);

        // JWT Filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
