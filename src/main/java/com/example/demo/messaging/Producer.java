package com.example.demo.messaging;

import com.example.demo.common.GeneralException;
import com.example.demo.device.model.DeviceLog;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

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
            throw new GeneralException("Interrupted while enqueueing", e);
        }
    }
}
