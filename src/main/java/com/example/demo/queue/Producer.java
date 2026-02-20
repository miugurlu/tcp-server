package com.example.demo.queue;

import com.example.demo.model.DeviceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * DeviceLog'u kuyruğa ekler (enqueue). TcpServerService bu sınıfı kullanır.
 */
@Component
public class Producer {

    @Autowired  // Spring bu alanı context'teki uygun bean ile otomatik doldurur
    private BlockingQueue<DeviceLog> deviceLogQueue;

    /**
     * Log'u kuyruğa ekler; Consumer arka planda alıp MongoDB'ye yazar.
     */
    public void enqueue(DeviceLog log) {
        try {
            deviceLogQueue.put(log);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Kuyruğa eklenirken kesinti", e);
        }
    }
}
