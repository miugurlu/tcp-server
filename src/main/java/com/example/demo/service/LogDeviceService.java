package com.example.demo.service;

import com.example.demo.mapper.IDeviceDataMapper;
import com.example.demo.model.DeviceData;
import com.example.demo.model.DeviceLog;
import com.example.demo.repository.IDeviceDataRepository;
import com.example.demo.repository.IDeviceLogRepository;
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
