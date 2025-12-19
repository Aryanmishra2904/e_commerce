package com.aryan.e_commerce.payment;

import com.aryan.e_commerce.inventory.InventoryService;
import com.aryan.e_commerce.order.Order;
import com.aryan.e_commerce.order.OrderRepository;
import com.aryan.e_commerce.order.OrderStatus;
import com.aryan.e_commerce.order.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    public void handlePaymentCaptured(
            String razorpayOrderId,
            String razorpayPaymentId,
            String signature,
            String payerName,
            String email,
            String contact,
            String method
    ) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // 🔁 Idempotency
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            log.info("Payment already processed: {}", razorpayPaymentId);
            return;
        }

        Order order = orderRepository.findById(payment.getOrderId())
                .orElseThrow();

        // 🔐 Amount validation
        if (!payment.getAmount().equals(order.getTotalAmount())) {
            throw new RuntimeException("Payment amount mismatch");
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(signature);
        payment.setPayerName(payerName);
        payment.setPayerEmail(email);
        payment.setPayerContact(contact);
        payment.setMethod(method);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        order.setPaymentStatus(PaymentStatus.SUCCESS);
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        inventoryService.deductStock(order);

        log.info("Payment SUCCESS for order {}", order.getId());
    }

    public void handlePaymentFailed(String razorpayOrderId) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow();

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        log.warn("Payment FAILED for order {}", payment.getOrderId());
    }
}
