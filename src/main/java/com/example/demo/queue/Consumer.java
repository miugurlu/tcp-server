package com.example.demo.queue;

import com.example.demo.model.DeviceLog;
import com.example.demo.service.LogDeviceService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * Kuyruktan DeviceLog alıp MongoDB'ye yazar.
 */
@Log4j2
@Component
public class Consumer {

    private final BlockingQueue<DeviceLog> deviceLogQueue;
    private final LogDeviceService logDeviceService;

    public Consumer(BlockingQueue<DeviceLog> deviceLogQueue, LogDeviceService logDeviceService){
        this.deviceLogQueue = deviceLogQueue;
        this.logDeviceService = logDeviceService;
    }

    @PostConstruct
    public void startConsuming() {
        new Thread(() -> {
            while (true) {
                try {
                    DeviceLog deviceLog = deviceLogQueue.take();
                    logDeviceService.logDevice(deviceLog);
                    log.info("Veri MongoDB'ye kaydedildi.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("Kayıt hatası: {}", e.getMessage(), e);
                }
            }
        }, "device-log-consumer").start();
    }
}
