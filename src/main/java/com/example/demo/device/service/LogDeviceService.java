package com.example.demo.device.service;

import com.example.demo.device.mapper.IDeviceDataMapper;
import com.example.demo.device.model.DeviceData;
import com.example.demo.device.model.DeviceLog;
import com.example.demo.device.repository.IDeviceDataRepository;
import com.example.demo.device.repository.IDeviceLogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogDeviceService {


    private final IDeviceLogRepository deviceLogRepository;

    private final IDeviceDataRepository deviceDataRepository;

    private final IDeviceDataMapper deviceDataMapper;

    public LogDeviceService(IDeviceLogRepository deviceLogRepository, IDeviceDataRepository deviceDataRepository, IDeviceDataMapper deviceDataMapper){
        this.deviceLogRepository = deviceLogRepository;
        this.deviceDataRepository = deviceDataRepository;
        this.deviceDataMapper = deviceDataMapper;
    }

    public void logDevice(DeviceLog deviceLog) {
        if (deviceLog != null) {
            deviceLogRepository.save(deviceLog);
            DeviceData current = deviceDataMapper.toDeviceData(deviceLog);
            if (current != null && current.getId() != null) {
                deviceDataRepository.save(current);
            }
        }
    }
}
