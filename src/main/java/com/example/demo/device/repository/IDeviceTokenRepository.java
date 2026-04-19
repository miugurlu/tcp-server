package com.example.demo.device.repository;

import com.example.demo.device.model.DeviceToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeviceTokenRepository extends MongoRepository<DeviceToken, String> {
}
