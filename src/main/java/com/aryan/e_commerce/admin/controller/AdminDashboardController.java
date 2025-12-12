package com.aryan.e_commerce.admin.controller;

import com.aryan.e_commerce.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/overview")
    public ResponseEntity<?> overview() {
        return ResponseEntity.ok(Map.of(
                "totalProducts", adminService.totalProducts(),
                "totalOrders", adminService.totalOrders()
                // add users if you inject UserRepository into AdminService
        ));
    }
}
