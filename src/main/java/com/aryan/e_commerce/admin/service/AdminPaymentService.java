package com.aryan.e_commerce.admin.service;
import com.aryan.e_commerce.admin.dto.AdminPaymentResponse;
import com.aryan.e_commerce.payment.Payment;
import com.aryan.e_commerce.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminPaymentService {

    private final PaymentRepository paymentRepository;

    // =========================
    // GET ALL PAYMENTS (PAGED)
    // =========================
    public Page<AdminPaymentResponse> getAllPayments(int page, int size) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt").descending()
        );

        return paymentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    public AdminPaymentResponse getPaymentById(String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return mapToResponse(payment);
    }

    // =========================
    // MAPPER
    // =========================
    private AdminPaymentResponse mapToResponse(Payment payment) {

        return AdminPaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMode(payment.getMode())
                .transactionId(payment.getRazorpayPaymentId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .payerName(payment.getPayerName())
                .payerEmail(payment.getPayerEmail())
                .payerContact(payment.getPayerContact())
                .method(payment.getMethod())
                .bank(payment.getBank())
                .wallet(payment.getWallet())
                .createdAt(payment.getCreatedAt())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
