package com.example.demo.queue;

import com.example.demo.model.DeviceLog;
import com.example.demo.repository.IDeviceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * Kuyruktan DeviceLog alıp MongDB'ye yazar. Uygulama başlarken ayrı thread'de döngüye girer.
 */
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
                    DeviceLog log = deviceLogQueue.take();
                    if (log != null) {
                        repository.save(log);
                    }
                    System.out.println("Veri MongoDB'ye kaydedildi.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Kayıt hatası - " + e.getMessage());
                }
            }
        }, "device-log-consumer").start();
    }
}
