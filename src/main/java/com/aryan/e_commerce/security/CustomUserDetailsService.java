package com.aryan.e_commerce.security;

import com.aryan.e_commerce.user.User;
import com.aryan.e_commerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    // ⭐ Spring Security uses this when authenticating by email (login)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );

        return new SecurityUser(user);
    }

    // ⭐ JWT filter uses this to load user by ID extracted from JWT subject
    public UserDetails loadUserById(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with ID: " + id)
                );

        return new SecurityUser(user);
    }
}
