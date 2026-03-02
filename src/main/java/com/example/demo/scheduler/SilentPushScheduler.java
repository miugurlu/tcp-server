package com.example.demo.scheduler;

import com.example.demo.model.DeviceToken;
import com.example.demo.repository.IDeviceTokenRepository;
import com.example.demo.service.ApnsPushService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class SilentPushScheduler {

    @Autowired
    private IDeviceTokenRepository deviceTokenRepository;

    @Autowired
    private ApnsPushService apnsPushService;

    @Scheduled(cron = "0 0,30 * * * *")
    public void sendSilentPushToAllDevices(){

        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAll();

        for (DeviceToken deviceToken : deviceTokenList ){
            try {
                apnsPushService.sendSilentPush(deviceToken.getDeviceToken());
            } catch (Exception e) {
                log.error("Error sending silent push to device: {}", deviceToken.getDeviceToken(), e);
            }
        }
    }
}
