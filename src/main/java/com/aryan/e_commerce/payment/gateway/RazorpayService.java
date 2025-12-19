package com.aryan.e_commerce.payment.gateway;

import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayClient razorpayClient;

    public RazorpayService(
            @Value("${razorpay.key}") String key,
            @Value("${razorpay.secret}") String secret
    ) throws RazorpayException {
        this.razorpayClient = new RazorpayClient(key, secret);
    }

    public Order createRazorpayOrder(Double amount) throws RazorpayException {

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // paise
        options.put("currency", "INR");
        options.put("payment_capture", 1);

        return razorpayClient.orders.create(options);
    }
}
