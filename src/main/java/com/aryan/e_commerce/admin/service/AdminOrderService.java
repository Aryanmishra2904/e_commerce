package com.aryan.e_commerce.admin.service;
import com.aryan.e_commerce.admin.dto.AdminOrderResponse;
import com.aryan.e_commerce.order.Order;
import com.aryan.e_commerce.order.*;
import com.aryan.e_commerce.order.OrderStatus;
import com.aryan.e_commerce.order.PaymentMode;
import com.aryan.e_commerce.order.PaymentStatus;
import com.aryan.e_commerce.payment.Payment;
import com.aryan.e_commerce.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public Page<AdminOrderResponse> getAllOrders(int page, int size) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("createdAt").descending());

        return orderRepository.findAll(pageable)
                .map(this::mapToAdminResponse);
    }

    public AdminOrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToAdminResponse(order);
    }

    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        validateTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);

        // ✅ COD payment becomes SUCCESS on delivery
        if (newStatus == OrderStatus.DELIVERED
                && order.getPaymentMode() == PaymentMode.COD) {

            order.setPaymentStatus(PaymentStatus.SUCCESS);

            paymentRepository.findByOrderId(order.getId())
                    .ifPresent(payment -> {
                        payment.setStatus(PaymentStatus.SUCCESS);
                        paymentRepository.save(payment);
                    });
        }

        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    private AdminOrderResponse mapToAdminResponse(Order order) {

        String transactionId = order.getPaymentId() == null
                ? null
                : paymentRepository.findById(order.getPaymentId())
                .map(Payment::getGatewayPaymentId)
                .orElse(null);

        return AdminOrderResponse.builder()
                .transactionId(transactionId)
                .build();


    }

    private void validateTransition(OrderStatus current, OrderStatus next) {

        if (current == OrderStatus.CANCELLED
                || current == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order status cannot be changed");
        }

        if (current == OrderStatus.SHIPPED && next == OrderStatus.PLACED) {
            throw new RuntimeException("Invalid order transition");
        }
    }
}
