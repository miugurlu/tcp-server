package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Cihazın en güncel verisi. device_data koleksiyonunda saklanır.
 * Cihaz başına tek doküman: _id = identity.identifierForVendor (IDFV), her yeni log geldiğinde güncellenir.
 */
@Document(collection = "device_data")
@Data
@NoArgsConstructor
public class DeviceData {

    @Id
    private String id;
    private DeviceLog.Identity identity;
    private DeviceLog.Resources resources;
    private DeviceLog.Power power;
    private DeviceLog.Network network;
    private DeviceLog.Location location;
    private LocalDateTime recordTime;
}
