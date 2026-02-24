package com.example.demo.queue;

import com.example.demo.model.DeviceLog;
import com.example.demo.repository.IDeviceRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * Kuyruktan DeviceLog alıp MongDB'ye yazar. Uygulama başlarken ayrı thread'de döngüye girer.
 */
@Log4j2
@Component
public class Consumer {

    @Autowired
    private BlockingQueue<DeviceLog> deviceLogQueue;

    @Autowired
    private IDeviceRepository repository;

    @PostConstruct
    public void startConsuming() {
        new Thread(() -> {
            while (true) {
                try {
                    DeviceLog deviceLog = deviceLogQueue.take();
                    if (deviceLog != null) {
                        repository.save(deviceLog);
                    }
                    log.info("Veri MongoDB'ye kaydedildi.");
                    //TODO log ve burada direkt repositorye gitme, bir service olsun. Repoyu encapsule et (?)
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
