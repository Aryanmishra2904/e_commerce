package com.aryan.e_commerce.security;

import com.aryan.e_commerce.user.User;
import com.aryan.e_commerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    // Used by Spring Security (login with email)
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new SecurityUser(user);
    }

    // ✅ Used by JWT filter (load by ID)
    public UserDetails loadUserById(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new SecurityUser(user);
    }
}
