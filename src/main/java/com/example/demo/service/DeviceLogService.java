package com.example.demo.service;

import com.example.demo.mapper.IDeviceDataMapper;
import com.example.demo.model.DeviceData;
import com.example.demo.model.DeviceLog;
import com.example.demo.repository.IDeviceDataRepository;
import com.example.demo.repository.IDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceLogService {


    //TODO repository -> deviceLogRepository + LogDeviceService
    @Autowired
    private IDeviceRepository repository;

    @Autowired
    private IDeviceDataRepository deviceDataRepository;

    @Autowired
    private IDeviceDataMapper deviceDataMapper;

    public void logDevice(DeviceLog deviceLog) {
        if (deviceLog != null) {
            repository.save(deviceLog);
            DeviceData current = deviceDataMapper.toDeviceData(deviceLog);
            if (current != null && current.getId() != null) {
                deviceDataRepository.save(current);
            }
        }
    }
}
