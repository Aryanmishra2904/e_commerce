package com.aryan.e_commerce.order;
public enum OrderStatus {
    PLACED,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    RETURN_REQUESTED,   // 🔑 NEW
    RETURN_APPROVED,    // 🔑 NEW
    RETURN_REJECTED,    // 🔑 NEW
    RETURNED,
    CANCELLED
}
