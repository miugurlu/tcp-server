package com.example.demo.device.service;

import com.example.demo.device.model.DeviceToken;
import com.example.demo.device.repository.IDeviceTokenRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DeviceTokenService {

    private final IDeviceTokenRepository deviceTokenRepository;

    public DeviceTokenService(IDeviceTokenRepository deviceTokenRepository){
        this.deviceTokenRepository = deviceTokenRepository;
    }

    public void registerToken(DeviceToken deviceToken) {
        if (deviceToken == null || deviceToken.getId() == null || deviceToken.getToken() == null) {
            log.warn("registerToken: deviceToken or required fields are null, skipping registration.");
            return;
        }
        deviceTokenRepository.save(deviceToken);
        log.info("Token saved/updated: deviceId={}", deviceToken.getId());
    }
}
