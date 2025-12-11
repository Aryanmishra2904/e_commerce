package com.aryan.e_commerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Public auth routes
                        .requestMatchers(
                                "/auth/signup",
                                "/auth/login",
                                "/auth/logout",
                                "/auth/forgot-password",
                                "/auth/reset-password",
                                "/auth/send-otp",
                                "/auth/verify-otp"
                        ).permitAll()

                        // Admin-only routes
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // All other routes require authentication
                        .anyRequest().authenticated()
                )

                // No sessions → fully stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(form -> form.disable());

        // Add JWT validation filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
