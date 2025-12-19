package com.aryan.e_commerce.admin.controller;
import com.aryan.e_commerce.admin.service.AdminPaymentService;
import com.aryan.e_commerce.admin.dto.AdminPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    // ============================
    // GET ALL PAYMENTS (PAGINATED)
    // ============================
    @GetMapping
    public Page<AdminPaymentResponse> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminPaymentService.getAllPayments(page, size);
    }

    // ============================
    // GET PAYMENT BY ID
    // ============================
    @GetMapping("/{paymentId}")
    public AdminPaymentResponse getPaymentById(
            @PathVariable String paymentId
    ) {
        return adminPaymentService.getPaymentById(paymentId);
    }
}
