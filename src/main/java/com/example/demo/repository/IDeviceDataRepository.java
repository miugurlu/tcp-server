package com.example.demo.repository;

import com.example.demo.model.DeviceData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * device_data koleksiyonu: cihaz başına tek doküman (güncel veri). Aynı deviceId ile save() = upsert.
 */
@Repository
public interface IDeviceDataRepository extends MongoRepository<DeviceData, String> {
}
