package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("identity")
    private Identity identity;

    @JsonProperty("resources")
    private Resources resources;

    @JsonProperty("power")
    private Power power;

    @JsonProperty("network")
    private Network network;

    @JsonProperty("location")
    private Location location;

    private LocalDateTime recordTime;

    // İç içe dokümanlar (MongoDB'de gömülü nesne olarak saklanır)

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identity {
        @JsonProperty("deviceName")
        private String deviceName;

        @JsonProperty("systemName")
        private String systemName;

        @JsonProperty("systemVersion")
        private String systemVersion;

        @JsonProperty("model")
        private String model;

        @JsonProperty("localizedModel")
        private String localizedModel;

        @JsonProperty("userInterfaceIdiom")
        private String userInterfaceIdiom;

        @JsonProperty("identifierForVendor")
        private String identifierForVendor;

        @JsonProperty("machineIdentifier")
        private String machineIdentifier;

        @JsonProperty("isMultiTaskingSupported")
        private Boolean isMultiTaskingSupported;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Resources {
        @JsonProperty("physicalMemoryGB")
        private String physicalMemoryGB;

        @JsonProperty("processorCountActive")
        private Integer processorCountActive;

        @JsonProperty("processorCountTotal")
        private Integer processorCountTotal;

        @JsonProperty("systemUptime")
        private String systemUptime;

        @JsonProperty("totalDiskSpaceGB")
        private String totalDiskSpaceGB;

        @JsonProperty("freeDiskSpaceGB")
        private String freeDiskSpaceGB;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Power {
        @JsonProperty("batteryLevel")
        private String batteryLevel;

        @JsonProperty("batteryState")
        private String batteryState;

        @JsonProperty("thermalState")
        private String thermalState;

        @JsonProperty("orientation")
        private String orientation;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        @JsonProperty("connectionType")
        private String connectionType;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        @JsonProperty("latitude")
        private Double latitude;

        @JsonProperty("longitude")
        private Double longitude;

        @JsonProperty("altitude")
        private Double altitude;

        @JsonProperty("timestamp")
        private String timestamp;
    }
}
