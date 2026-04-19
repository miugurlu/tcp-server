package com.example.demo.device.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
