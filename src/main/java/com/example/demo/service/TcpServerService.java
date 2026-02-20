package com.example.demo.service;

import com.example.demo.model.DeviceLog;
import com.example.demo.queue.Producer;
// JSON okuma (Jackson)
import com.fasterxml.jackson.databind.ObjectMapper;
// Spring: uygulama başlarken metod çalıştırma
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// TCP sunucu ve giriş akışı
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
@Service // Spring: Bu sınıf bir servis bean'i; gerekli yerlere enjekte edilebilir
public class TcpServerService {

    /** Gelen log'u kuyruğa ekler; Consumer arka planda MongoDB'ye yazar */
    @Autowired
    private Producer producer;

    /** Jackson ObjectMapper; Spring'in sağladığı Java 8 tarih (LocalDateTime vb.) desteği dahil. */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Uygulama ayağa kalkınca bir kez çalışır; 8080 portunda TCP sunucusunu başlatır.
     * Ana thread'i bloklamamak için ayrı bir thread'de çalışır.
     */
    @PostConstruct  // Bean oluşturulduktan hemen sonra bu metod bir kez otomatik çalışır (sunucuyu başlatmak için)
    public void startTcpServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8080, 0, InetAddress.getByName("0.0.0.0"))) {
                System.out.println("TCP Server 8080 portunda dinleniyor...");
                while (true) {
                    // Yeni bağlantı gelene kadar bekler; gelince Socket döner
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Bağlanan bir istemciden tek satır JSON okuyup DeviceLog'a çevirir ve veritabanına kaydeder.
     */
    private void handleClient(Socket socket) {
        final ObjectMapper mapper = this.objectMapper;
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                // Cihaz tek satır JSON göndermeli
                String inputLine = reader.readLine();
                System.out.println("Cihazdan gelen ham veri: " + inputLine);

                // JSON metnini DeviceLog nesnesine çevir (Jackson; Spring ObjectMapper Java 8 tarih tiplerini destekler)
                DeviceLog log = mapper.readValue(inputLine, DeviceLog.class);
                if (log.getRecordTime() == null) {
                    log.setRecordTime(LocalDateTime.now());
                }
                producer.enqueue(log);
                System.out.println("Veri kuyruğa eklendi.");
            } catch (Exception e) {
                System.out.println("Veri okunurken hata oluştu: " + e.getMessage());
            }
        }).start();
    }
}