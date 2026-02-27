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

    /** MongoDB _id = cihaz kimliği (deviceId/IDFV); aynı cihaz tekrar gelince güncelleme (upsert). */
    @Id
    private String id;
    private DeviceLog.Identity identity;
    private DeviceLog.Resources resources;
    private DeviceLog.Power power;
    private DeviceLog.Network network;
    private DeviceLog.Location location;
    private LocalDateTime recordTime;

    /**
     * DeviceLog'dan güncel snapshot oluşturur. id = identity.identifierForVendor (IDFV); cihaz başına tek kayıt.
     * identity veya identifierForVendor yoksa null döner (device_data'ya yazılmaz).
     */
    public static DeviceData from(DeviceLog log) {
        if (log == null || log.getIdentity() == null || log.getIdentity().getIdentifierForVendor() == null) {
            return null;
        }
        DeviceData d = new DeviceData();
        d.setId(log.getIdentity().getIdentifierForVendor());
        d.setIdentity(log.getIdentity());
        d.setResources(log.getResources());
        d.setPower(log.getPower());
        d.setNetwork(log.getNetwork());
        d.setLocation(log.getLocation());
        d.setRecordTime(log.getRecordTime());
        return d;
    }
}
