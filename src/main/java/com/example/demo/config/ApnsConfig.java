package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "apns")
@Data
public class ApnsConfig {
    private String keyPath;
    private String keyId;
    private String teamId;
    private String bundleId;
    private Boolean sandbox;
}
