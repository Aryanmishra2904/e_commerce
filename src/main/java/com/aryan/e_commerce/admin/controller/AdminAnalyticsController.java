package com.aryan.e_commerce.admin.controller;

import com.aryan.e_commerce.admin.dto.DashboardOverviewDto;
import com.aryan.e_commerce.admin.dto.OrderStatusStatsDto;
import com.aryan.e_commerce.admin.dto.TopProductDto;
import com.aryan.e_commerce.admin.dto.UserStatsDto;
import com.aryan.e_commerce.admin.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

    private final AdminAnalyticsService service;

    @GetMapping("/overview")
    public DashboardOverviewDto overview() {
        return service.getOverview();
    }

    @GetMapping("/orders/status")
    public List<OrderStatusStatsDto> orderStatus() {
        return service.getOrderStatusStats();
    }

    @GetMapping("/products/top")
    public List<TopProductDto> topProducts() {
        return service.getTopSellingProducts();
    }

    @GetMapping("/users")
    public UserStatsDto userStats() {
        return service.getUserStats();
    }
}
