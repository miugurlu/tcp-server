package com.example.demo.queue;

import com.example.demo.common.GeneralException;
import com.example.demo.model.DeviceLog;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

/**
 * DeviceLog'u kuyruğa ekler (enqueue). TcpServerService bu sınıfı kullanır.
 */
@Component
public class Producer {

    private final BlockingQueue<DeviceLog> deviceLogQueue;

    public Producer(BlockingQueue<DeviceLog> deviceLogQueue){
        this.deviceLogQueue = deviceLogQueue;
    }

    public void enqueue(DeviceLog log) {
        try {
            deviceLogQueue.put(log);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GeneralException("Kuyruğa eklenirken kesinti", e);
        }
    }
}
