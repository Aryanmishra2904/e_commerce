package com.aryan.e_commerce.admin.controller;

import com.aryan.e_commerce.admin.dto.AdminOrderResponse;
import com.aryan.e_commerce.admin.service.AdminOrderService;
import com.aryan.e_commerce.order.Order;
import com.aryan.e_commerce.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final AdminOrderService service;

    @GetMapping
    public Page<AdminOrderResponse> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAllOrders(page, size);
    }

    @GetMapping("/{id}")
    public AdminOrderResponse getOrder(@PathVariable String id) {
        return service.getOrderById(id);
    }

    @PatchMapping("/{id}/status")
    public Order updateStatus(
            @PathVariable String id,
            @RequestParam OrderStatus status
    ) {
        return service.updateOrderStatus(id, status);
    }
}
