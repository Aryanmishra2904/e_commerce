package com.aryan.e_commerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardOverviewDto {

    private double totalRevenue;
    private long totalOrders;
    private long totalUsers;
}

