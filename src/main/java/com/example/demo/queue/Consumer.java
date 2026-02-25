package com.example.demo.queue;

import com.example.demo.model.DeviceLog;
import com.example.demo.service.DeviceLogService;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * Kuyruktan DeviceLog alıp MongoDB'ye yazar. Uygulama başlarken ayrı thread'de döngüye girer.
 */
@Log4j2
@Component
public class Consumer {

    @Autowired
    private BlockingQueue<DeviceLog> deviceLogQueue;

    @Autowired
    private DeviceLogService deviceLogService;

    @PostConstruct
    public void startConsuming() {
        new Thread(() -> {
            while (true) {
                try {
                    DeviceLog deviceLog = deviceLogQueue.take();
                    deviceLogService.logDevice(deviceLog);
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
