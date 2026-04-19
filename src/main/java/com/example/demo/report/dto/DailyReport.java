package com.example.demo.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyReport {

    private LocalDate reportDate;

    private List<DeviceReportEntry> devices;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceReportEntry{
        private String deviceName;
        private String systemName;
        private String systemVersion;
        private String  identifierForVendor;
        private List<DeviceReadingRow> readings;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceReadingRow{
        private String physicalMemoryGB;
        private Integer processorCountActive;
        private Integer processorCountTotal;
        private String systemUptime;
        private String totalDiskSpaceGB;
        private String freeDiskSpaceGB;
        private String batteryLevel;
        private String batteryState;
        private String thermalState;
        private String connectionType;
        private LocalDateTime recordTime;
    }
}
