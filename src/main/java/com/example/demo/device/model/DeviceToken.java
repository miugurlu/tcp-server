package com.example.demo.device.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "device_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceToken {

    @Id
    private String id;

    private String token;

    public static DeviceToken of(String deviceId, String deviceToken) {
        DeviceToken t = new DeviceToken();
        t.setId(deviceId);
        t.setToken(deviceToken);
        return t;
    }
}
