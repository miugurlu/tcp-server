package com.example.demo.model;

// JSON okuma/yazma (Jackson): alan eşlemesi ve bilinmeyen alanları yok sayma
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
// JPA: veritabanı tablosu ve kolon eşlemesi
import jakarta.persistence.*;
// Lombok: getter/setter, constructor üretimi
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Cihazdan gelen tek bir log kaydını temsil eder.
 * Hem veritabanı tablosu (device_logs) hem de TCP'den gelen JSON ile eşleşir.
 */
@Entity  // JPA: Bu sınıf bir veritabanı tablosuyla eşlenecek (entity)
@Table(name = "device_logs")  // JPA: Tablo adı "device_logs" olsun
@JsonIgnoreProperties(ignoreUnknown = true)  // Jackson: JSON'da tanımsız alan varsa hata verme, atla
@Data  // Lombok: getter, setter, equals, hashCode, toString üret
@NoArgsConstructor  // Lombok: parametresiz constructor üret (JPA/Jackson için gerekli)
public class DeviceLog {

    @Id  // JPA: Bu alan birincil anahtar (primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA: Değer veritabanında otomatik artsın
    private Long id;

    /** Cihaz kimliği: ad, sistem, model vb. (JSON'da "identity" alt nesnesi) */
    @Embedded  // JPA: Bu alan ayrı tablo değil, bu tabloya kolonlar olarak gömülü
    @AttributeOverrides({
            @AttributeOverride(name = "deviceName", column = @Column(name = "identity_device_name")),
            @AttributeOverride(name = "systemName", column = @Column(name = "identity_system_name")),
            @AttributeOverride(name = "systemVersion", column = @Column(name = "identity_system_version")),
            @AttributeOverride(name = "model", column = @Column(name = "identity_model")),
            @AttributeOverride(name = "localizedModel", column = @Column(name = "identity_localized_model")),
            @AttributeOverride(name = "userInterfaceIdiom", column = @Column(name = "identity_user_interface_idiom")),
            @AttributeOverride(name = "identifierForVendor", column = @Column(name = "identity_identifier_for_vendor")),
            @AttributeOverride(name = "machineIdentifier", column = @Column(name = "identity_machine_identifier")),
            @AttributeOverride(name = "isMultiTaskingSupported", column = @Column(name = "identity_multi_tasking_supported"))
    })  // JPA: Gömülü sınıfın her alanı için tablodaki kolon adını belirle
    @JsonProperty("identity")  // Jackson: JSON'daki "identity" alt nesnesi bu alana eşlensin
    private Identity identity;

    /** Donanım kaynakları: RAM, işlemci sayısı, disk, uptime (JSON'da "resources") */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "physicalMemoryGB", column = @Column(name = "resources_physical_memory_gb")),
            @AttributeOverride(name = "processorCountActive", column = @Column(name = "resources_processor_count_active")),
            @AttributeOverride(name = "processorCountTotal", column = @Column(name = "resources_processor_count_total")),
            @AttributeOverride(name = "systemUptime", column = @Column(name = "resources_system_uptime")),
            @AttributeOverride(name = "totalDiskSpaceGB", column = @Column(name = "resources_total_disk_space_gb")),
            @AttributeOverride(name = "freeDiskSpaceGB", column = @Column(name = "resources_free_disk_space_gb"))
    })
    @JsonProperty("resources")
    private Resources resources;

    /** Pil, termal durum, ekran yönü (JSON'da "power") */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "batteryLevel", column = @Column(name = "power_battery_level")),
            @AttributeOverride(name = "batteryState", column = @Column(name = "power_battery_state")),
            @AttributeOverride(name = "thermalState", column = @Column(name = "power_thermal_state")),
            @AttributeOverride(name = "orientation", column = @Column(name = "power_orientation"))
    })
    @JsonProperty("power")
    private Power power;

    /** Ağ bilgisi: bağlantı türü (Wi‑Fi, cellular vb.) (JSON'da "network") */
    @Embedded
    @AttributeOverride(name = "connectionType", column = @Column(name = "network_connection_type"))  // Tek alanlı gömülü sınıf için kolon adı
    @JsonProperty("network")
    private Network network;

    /** Konum: enlem, boylam, yükseklik (JSON'da "location") */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "location_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "location_longitude")),
            @AttributeOverride(name = "altitude", column = @Column(name = "location_altitude")),
            @AttributeOverride(name = "timestamp", column = @Column(name = "location_timestamp"))
    })
    @JsonProperty("location")
    private Location location;

    /** Kaydın sunucuya düştüğü an; ilk kayıtta otomatik atanır */
    @Column(name = "record_time", nullable = false)  // JPA: Kolon adı "record_time", boş olamaz
    private LocalDateTime recordTime;

    /** Veritabanına ilk yazılmadan hemen önce çalışır; recordTime = şu an */
    @PrePersist  // JPA: Bu nesne ilk kez DB'ye yazılmadan hemen önce bu metod çalışır
    protected void onCreate() {
        recordTime = LocalDateTime.now();
    }

    // ========== Gömülü sınıflar: tabloda ayrı satır yok, DeviceLog kolonlarına yayılır ==========

    /** Cihaz kimliği: ad, sistem adı/sürümü, model, vendor identifier vb. (iOS uyumlu) */
    @Embeddable  // JPA: Bu sınıf tek başına tablo değil, başka entity içine gömülecek
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identity {
        @JsonProperty("deviceName")  // Jackson: JSON'daki "deviceName" bu alana
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

    /** Donanım kaynakları: RAM (GB), işlemci sayısı, uptime, disk alanı */
    @Embeddable  // JPA: Bu sınıf başka entity içine gömülü, ayrı tablo yok
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Resources {
        @JsonProperty("physicalMemoryGB")  // Jackson: JSON alan adı → Java alanı
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

    /** Pil seviyesi, pil/termal durumu, ekran yönü */
    @Embeddable
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

    /** Ağ bağlantı türü (Wi‑Fi, cellular vb.) */
    @Embeddable
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Network {
        @JsonProperty("connectionType")
        private String connectionType;
    }

    /** Konum: enlem, boylam, yükseklik ve konum zaman damgası */
    @Embeddable
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
