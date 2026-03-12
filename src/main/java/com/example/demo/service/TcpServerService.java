package com.example.demo.service;

import com.example.demo.common.GeneralException;
import com.example.demo.dto.TcpInboundMessage;
import com.example.demo.model.DeviceLog;
import com.example.demo.model.DeviceToken;
import com.example.demo.queue.Producer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
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

    private final Producer producer;

    private final DeviceTokenService deviceTokenService;

    private final ObjectMapper objectMapper;

    public TcpServerService(Producer producer, DeviceTokenService deviceTokenService, ObjectMapper objectMapper){
        this.producer = producer;
        this.deviceTokenService = deviceTokenService;
        this.objectMapper = objectMapper;
    }

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
            } catch (IOException e) {
                log.error("TCP sunucu hatası", new GeneralException("TCP sunucu hatası", e));
            }
        }, "tcp-server-acceptor").start();
    }

    /**
     * Bağlanan bir istemciden tek satır JSON okur; DTO'ya parse edip type'a göre yönlendirir.
     */
    private void handleClient(Socket socket) {
        final ObjectMapper mapper = this.objectMapper;
        String clientName = "tcp-client-" + socket.getRemoteSocketAddress();
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                StringBuilder inputLine = new StringBuilder();
                inputLine.append(reader.readLine());
                log.debug("Cihazdan gelen ham veri: {}", inputLine);

                JsonNode root = mapper.readTree(inputLine.toString());
                if (root == null) {
                    return;
                }
                TcpInboundMessage dto = new TcpInboundMessage(
                        root.path("type").asText(null),
                        root
                );

                if ("register_token".equals(dto.getType())) {
                    String deviceId = dto.getPayload().path("deviceId").asText(null);
                    String deviceTokenValue = dto.getPayload().path("deviceToken").asText(null);
                    DeviceToken deviceToken = DeviceToken.of(deviceId, deviceTokenValue);
                    deviceTokenService.registerToken(deviceToken);
                    return;
                }

                if ("device_inventory".equals(dto.getType())) {
                    DeviceLog deviceLog = mapper.treeToValue(dto.getPayload(), DeviceLog.class);
                    if (deviceLog.getRecordTime() == null) {
                        deviceLog.setRecordTime(LocalDateTime.now());
                    }
                    producer.enqueue(deviceLog);
                    log.info("Veri kuyruğa eklendi.");
                    return;
                }

                log.warn("Bilinmeyen mesaj tipi, atlanıyor: type={}", dto.getType());
            } catch (IOException e) {
                log.warn("Veri okunurken hata", new GeneralException("Veri okunamadı", e));
            }
        }, clientName).start();
    }
}