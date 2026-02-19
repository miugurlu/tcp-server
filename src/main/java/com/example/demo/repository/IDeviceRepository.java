package com.example.demo.repository;

import com.example.demo.model.DeviceLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * DeviceLog kayıtları için MongoDB erişim arayüzü.
 * MongoRepository sayesinde save(), findById(), findAll() vb. otomatik gelir.
 */
@Repository
public interface IDeviceRepository extends MongoRepository<DeviceLog, String> {
}