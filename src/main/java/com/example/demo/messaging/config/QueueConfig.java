package com.example.demo.messaging.config;

import com.example.demo.device.model.DeviceLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<DeviceLog> deviceLogQueue() {
        return new LinkedBlockingQueue<>();
    }
}
