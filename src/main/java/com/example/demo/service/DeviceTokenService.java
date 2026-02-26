package com.example.demo.service;

import com.example.demo.model.DeviceToken;
import com.example.demo.repository.IDeviceTokenRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cihaz push token kaydı. device_tokens koleksiyonunda deviceId'ye göre tek kayıt tutulur (upsert).
 */
@Log4j2
@Service
public class DeviceTokenService {

    @Autowired
    private IDeviceTokenRepository deviceTokenRepository;

    /**
     * deviceId ve deviceToken'ı kaydeder veya aynı deviceId varsa günceller.
     */
    public void registerToken(String deviceId, String deviceToken) {
        if (deviceId == null || deviceToken == null) {
            log.warn("registerToken: deviceId veya deviceToken null, kayıt atlanıyor.");
            return;
        }
        deviceTokenRepository.save(DeviceToken.of(deviceId, deviceToken));
        log.info("Token kaydedildi/güncellendi: deviceId={}", deviceId);
    }
}
