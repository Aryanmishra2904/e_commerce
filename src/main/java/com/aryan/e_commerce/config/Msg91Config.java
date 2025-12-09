package com.aryan.e_commerce.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "msg91")
public class Msg91Config {
    private String authKey;
    private String senderId;
    private String templateId;
    private String route;
    private String country;
}
