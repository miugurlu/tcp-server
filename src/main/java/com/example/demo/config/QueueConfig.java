package com.example.demo.config;

import com.example.demo.model.DeviceLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Paylaşılan kuyruk: Producer enqueue eder, Consumer take ile alıp MongoDB'ye yazar.
 */
@Configuration  // Spring: Bu sınıf bean tanımları içerir; içindeki @Bean metodları context ayağa kalkarken çalışır, dönen nesneler bean olarak kaydedilir
public class QueueConfig {

    @Bean  // Bean: Spring'in yönettiği tek nesne; Producer ve Consumer aynı kuyruğu paylaşır
    public BlockingQueue<DeviceLog> deviceLogQueue() {
        return new LinkedBlockingQueue<>();
    }
}
