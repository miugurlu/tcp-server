package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Cihaz push token bilgisi. device_tokens koleksiyonunda saklanır.
 * Aynı deviceId tekrar kaydedilirse doküman güncellenir (upsert).
 */
@Document(collection = "device_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceToken {

    /** MongoDB _id ve tekil anahtar; deviceId ile aynı tutulur (upsert için). */
    @Id
    private String id;

    private String token;

    /**
     * Upsert için: id alanı deviceId olarak set edilir, böylece aynı cihaz tekrar gelince güncelleme yapılır.
     */
    public static DeviceToken of(String deviceId, String deviceToken) {
        DeviceToken t = new DeviceToken();
        t.setId(deviceId);
        t.setToken(deviceToken);
        return t;
    }
}
