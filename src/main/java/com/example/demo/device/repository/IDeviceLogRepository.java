package com.example.demo.device.repository;

import com.example.demo.device.model.DeviceLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IDeviceLogRepository extends MongoRepository<DeviceLog, String> {

    List<DeviceLog> findByRecordTimeBetween(LocalDateTime start, LocalDateTime end);
}