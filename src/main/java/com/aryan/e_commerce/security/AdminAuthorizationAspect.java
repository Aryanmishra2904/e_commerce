package com.aryan.e_commerce.security;

import com.aryan.e_commerce.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AdminAuthorizationAspect {

    @Around("@annotation(AdminOnly)")
    public Object authorizeAdmin(ProceedingJoinPoint joinPoint) throws Throwable {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof SecurityUser)) {
            throw new RuntimeException("Unauthorized: No user authenticated");
        }

        SecurityUser user = (SecurityUser) auth.getPrincipal();

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied: Admin only");
        }

        return joinPoint.proceed();
    }
}
