package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.demo.model.DeviceData;
import com.example.demo.model.DeviceLog;

@Mapper(componentModel = "spring")
public interface IDeviceDataMapper {

    @Mapping(target = "id", source = "identity.identifierForVendor")
    DeviceData toDeviceData(DeviceLog log);
}
