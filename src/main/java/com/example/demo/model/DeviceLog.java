package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Cihazdan gelen tek bir log kaydını temsil eder.
 * MongoDB'de device_logs koleksiyonunda saklanır; TCP'den gelen JSON ile eşleşir.
 */
@Document(collection = "device_logs")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class DeviceLog {

    @Id
    private String id;
    private String deviceId;
    private Identity identity;
    private Resources resources;
    private Power power;
    private Network network;
    private Location location;
    private LocalDateTime recordTime;

    // İç içe dokümanlar (MongoDB'de gömülü nesne olarak saklanır)

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identity {
        private String deviceName;
        private String systemName;
        private String systemVersion;
        private String model;
        private String localizedModel;
        private String userInterfaceIdiom;
        private String identifierForVendor;
        private String machineIdentifier;
        private Boolean isMultiTaskingSupported;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Resources {
        private String physicalMemoryGB;
        private Integer processorCountActive;
        private Integer processorCountTotal;
        private String systemUptime;
        private String totalDiskSpaceGB;
        private String freeDiskSpaceGB;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Power {
        private String batteryLevel;
        private String batteryState;
        private String thermalState;
        private String orientation;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        private String connectionType;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private Double latitude;
        private Double longitude;
        private Double altitude;
        private LocalDateTime timestamp;
    }
}
