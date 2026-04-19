package com.example.demo.report.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.report.dto.DailyReport;
import com.example.demo.device.model.DeviceLog;

@Mapper(componentModel = "spring")
public interface IDailyReportMapper {

    @Mapping(target = "deviceName", source = "identity.deviceName")
    @Mapping(target = "systemName", source = "identity.systemName")
    @Mapping(target = "systemVersion", source = "identity.systemVersion")
    @Mapping(target = "identifierForVendor", source = "identity.identifierForVendor")
    @Mapping(target = "readings", ignore = true)
    DailyReport.DeviceReportEntry toDeviceReportEntry(DeviceLog log);

    @Mapping(target = "physicalMemoryGB", source = "resources.physicalMemoryGB")
    @Mapping(target = "processorCountActive", source = "resources.processorCountActive")
    @Mapping(target = "processorCountTotal", source = "resources.processorCountTotal")
    @Mapping(target = "systemUptime", source = "resources.systemUptime")
    @Mapping(target = "totalDiskSpaceGB", source = "resources.totalDiskSpaceGB")
    @Mapping(target = "freeDiskSpaceGB", source = "resources.freeDiskSpaceGB")
    @Mapping(target = "batteryLevel", source = "power.batteryLevel")
    @Mapping(target = "batteryState", source = "power.batteryState")
    @Mapping(target = "thermalState", source = "power.thermalState")
    @Mapping(target = "connectionType", source = "network.connectionType")
    DailyReport.DeviceReadingRow toDeviceReadingRow(DeviceLog log);
}
