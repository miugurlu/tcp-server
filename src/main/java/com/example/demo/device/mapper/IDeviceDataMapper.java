package com.example.demo.device.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.demo.device.model.DeviceData;
import com.example.demo.device.model.DeviceLog;

@Mapper(componentModel = "spring")
public interface IDeviceDataMapper {

    @Mapping(target = "id", source = "identity.identifierForVendor")
    DeviceData toDeviceData(DeviceLog log);
}
