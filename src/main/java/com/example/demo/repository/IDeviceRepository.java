package com.example.demo.repository;

import com.example.demo.model.DeviceLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DeviceLog kayıtları için MongoDB erişim arayüzü.
 * MongoRepository sayesinde save(), findById(), findAll() vb. otomatik gelir.
 */
@Repository
public interface IDeviceRepository extends MongoRepository<DeviceLog, String> {

    List<DeviceLog> findByRecordTimeBetween(LocalDateTime start, LocalDateTime end);
}