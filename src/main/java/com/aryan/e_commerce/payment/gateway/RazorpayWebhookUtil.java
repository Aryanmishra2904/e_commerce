package com.aryan.e_commerce.payment.gateway;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class RazorpayWebhookUtil {

    public static boolean verifySignature(
            String payload,
            String actualSignature,
            String secret
    ) {
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec key =
                    new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            sha256.init(key);
            byte[] hash = sha256.doFinal(payload.getBytes());

            String expectedSignature =
                    Base64.getEncoder().encodeToString(hash);

            return expectedSignature.equals(actualSignature);

        } catch (Exception e) {
            return false;
        }
    }
}
