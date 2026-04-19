package com.example.demo.device.repository;

import com.example.demo.device.model.DeviceData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeviceDataRepository extends MongoRepository<DeviceData, String> {
}
