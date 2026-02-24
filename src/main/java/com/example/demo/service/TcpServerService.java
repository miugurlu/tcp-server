package com.example.demo.service;

import com.example.demo.model.DeviceLog;
import com.example.demo.queue.Producer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Cihazlardan TCP ile gelen JSON loglarını dinleyip veritabanına kaydeden servis.
 * 8080 portunda dinler; her bağlantıdan tek satır JSON okur ve MongoDB device_logs koleksiyonuna yazar.
 */
@Log4j2
@Service
public class TcpServerService {

    @Autowired
    private Producer producer;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Uygulama ayağa kalkınca bir kez çalışır; 8080 portunda TCP sunucusunu başlatır.
     * Ana thread'i bloklamamak için ayrı bir thread'de çalışır.
     */
    @PostConstruct
    public void startTcpServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8080, 0, InetAddress.getByName("0.0.0.0"))) {
                log.info("TCP Server 8080 portunda dinleniyor...");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (Exception e) {
                log.error("TCP sunucu hatası", e);
            }
        }, "tcp-server-acceptor").start();
    }

    /**
     * Bağlanan bir istemciden tek satır JSON okuyup DeviceLog'a çevirir ve veritabanına kaydeder.
     */
    private void handleClient(Socket socket) {
        final ObjectMapper mapper = this.objectMapper;
        String clientName = "tcp-client-" + socket.getRemoteSocketAddress();
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String inputLine = reader.readLine();
                log.debug("Cihazdan gelen ham veri: {}", inputLine);

                DeviceLog deviceLog = mapper.readValue(inputLine, DeviceLog.class);
                if (deviceLog.getRecordTime() == null) {
                    deviceLog.setRecordTime(LocalDateTime.now());
                }
                producer.enqueue(deviceLog);
                log.info("Veri kuyruğa eklendi.");
            } catch (Exception e) {
                log.warn("Veri okunurken hata oluştu: {}", e.getMessage(), e);
            }
        }, clientName).start();
    }
}