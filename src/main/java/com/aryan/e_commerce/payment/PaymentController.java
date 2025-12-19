package com.aryan.e_commerce.payment;

import com.aryan.e_commerce.order.Order;
import com.aryan.e_commerce.order.OrderRepository;
import com.aryan.e_commerce.order.PaymentMode;
import com.aryan.e_commerce.order.PaymentStatus;
import com.aryan.e_commerce.payment.gateway.RazorpayService;
import com.aryan.e_commerce.payment.gateway.RazorpayWebhookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final RazorpayService razorpayService;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;
    @PostMapping("/create/{orderId}")
    public com.razorpay.Order createPayment(@PathVariable String orderId) throws Exception {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        com.razorpay.Order razorpayOrder =
                razorpayService.createRazorpayOrder(order.getTotalAmount());

        paymentRepository.save(Payment.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .amount(order.getTotalAmount())
                .status(PaymentStatus.PENDING)
                .mode(PaymentMode.UPI)
                .razorpayOrderId(razorpayOrder.get("id"))
                .createdAt(LocalDateTime.now())
                .build());

        return razorpayOrder;
    }


    // ====================================
    // RAZORPAY WEBHOOK (SOURCE OF TRUTH)
    // ====================================
    @PostMapping("/webhook")
    public ResponseEntity<String> razorpayWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature
    ) {

        // 1️⃣ Verify webhook signature
        boolean isValid = RazorpayWebhookUtil.verifySignature(
                payload, signature, webhookSecret
        );

        if (!isValid) {
            log.error("Invalid Razorpay webhook signature");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid signature");
        }

        // 2️⃣ Parse webhook payload
        JSONObject json = new JSONObject(payload);
        String event = json.getString("event");

        JSONObject paymentEntity = json
                .getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");

        String razorpayOrderId = paymentEntity.getString("order_id");

        // 3️⃣ Handle events
        if ("payment.captured".equals(event)) {

            paymentService.handlePaymentCaptured(
                    razorpayOrderId,
                    paymentEntity.getString("id"),
                    signature,
                    paymentEntity.optString("email"),
                    paymentEntity.optString("email"),
                    paymentEntity.optString("contact"),
                    paymentEntity.getString("method")
            );

            log.info("Payment captured for Razorpay Order {}", razorpayOrderId);
        }

        if ("payment.failed".equals(event)) {
            paymentService.handlePaymentFailed(razorpayOrderId);
            log.warn("Payment failed for Razorpay Order {}", razorpayOrderId);
        }

        return ResponseEntity.ok("OK");
    }
}
