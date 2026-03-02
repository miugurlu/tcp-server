package com.example.demo.service;

import com.example.demo.config.ApnsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ApnsPushService {

    @Autowired
    private ApnsConfig apnsConfig;

    @Autowired
    private ApnsJwtService apnsJwtService;

    public void sendSilentPush(String deviceToken) throws Exception {
        String jwt = apnsJwtService.getToken();
        String payload = "{\"aps\":{\"content-available\":1}}";
        String url = "https://api.sandbox.push.apple.com/3/device/" + deviceToken;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + jwt)
            .header("apns-topic", apnsConfig.getBundleId())
            .header("apns-push-type", "background")
            .header("apns-priority", "5")
            .header("apns-expiration", "0")
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new Exception("Failed to send silent push: " + response.body());
        }
        log.info("Silent push sent successfully");
    }
}
