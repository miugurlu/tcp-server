package com.example.demo.service;

import com.example.demo.model.DeviceToken;
import com.example.demo.repository.IDeviceTokenRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Cihaz push token kaydı. device_tokens koleksiyonunda deviceId'ye göre tek kayıt tutulur (upsert).
 */
@Log4j2
@Service
public class DeviceTokenService {

    private final IDeviceTokenRepository deviceTokenRepository;

    public DeviceTokenService(IDeviceTokenRepository deviceTokenRepository){
        this.deviceTokenRepository = deviceTokenRepository;
    }

    /**
     * DeviceToken nesnesini kaydeder veya aynı deviceId varsa günceller (upsert).
     */
    public void registerToken(DeviceToken deviceToken) {
        if (deviceToken == null || deviceToken.getId() == null || deviceToken.getToken() == null) {
            log.warn("registerToken: deviceToken veya gerekli alanlar null, kayıt atlanıyor.");
            return;
        }
        deviceTokenRepository.save(deviceToken);
        log.info("Token kaydedildi/güncellendi: deviceId={}", deviceToken.getId());
    }
}
