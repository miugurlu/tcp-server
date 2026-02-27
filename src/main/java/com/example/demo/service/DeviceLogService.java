package com.example.demo.service;

import com.example.demo.model.DeviceData;
import com.example.demo.model.DeviceLog;
import com.example.demo.repository.IDeviceDataRepository;
import com.example.demo.repository.IDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceLogService {

    @Autowired
    private IDeviceRepository repository;

    @Autowired
    private IDeviceDataRepository deviceDataRepository;

    public void logDevice(DeviceLog deviceLog) {
        if (deviceLog != null) {
            repository.save(deviceLog);
            DeviceData current = DeviceData.from(deviceLog);
            if (current != null) {
                deviceDataRepository.save(current);
            }
        }
    }
}
