package com.example.demo.apns.scheduler;

import com.example.demo.device.model.DeviceToken;
import com.example.demo.device.repository.IDeviceTokenRepository;
import com.example.demo.apns.service.ApnsPushService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class SilentPushScheduler {

    private final IDeviceTokenRepository deviceTokenRepository;

    private final ApnsPushService apnsPushService;

    public SilentPushScheduler(IDeviceTokenRepository deviceTokenRepository, ApnsPushService apnsPushService){
        this.deviceTokenRepository = deviceTokenRepository;
        this.apnsPushService = apnsPushService;
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void sendSilentPushToAllDevices(){

        List<DeviceToken> deviceTokenList = deviceTokenRepository.findAll();

        for (DeviceToken deviceToken : deviceTokenList ){
            apnsPushService.sendSilentPush(deviceToken.getToken());
        }
    }
}
