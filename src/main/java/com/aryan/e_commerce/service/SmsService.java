package com.aryan.e_commerce.service;

import com.aryan.e_commerce.config.Msg91Config;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final Msg91Config config;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendSms(String phoneNumber, String otp) {

        // Correct OTP endpoint for MSG91
        String url = "https://control.msg91.com/api/v5/otp";

        HttpHeaders headers = new HttpHeaders();
        headers.set("authkey", config.getAuthKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request body for OTP API
        JSONObject body = new JSONObject();
        body.put("mobile", "91" + phoneNumber);      // Must be 10 digits
        body.put("otp", otp);                        // The OTP code
        body.put("template_id", config.getTemplateId()); // MSG91 OTP template ID

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        try {
            String response = restTemplate.postForObject(url, entity, String.class);

            System.out.println("📩 MSG91 Response: " + response);
            System.out.println("✅ OTP sent to: " + phoneNumber);

        } catch (Exception e) {
            System.err.println("❌ Error sending OTP via MSG91:");
            e.printStackTrace();
        }
    }
}
