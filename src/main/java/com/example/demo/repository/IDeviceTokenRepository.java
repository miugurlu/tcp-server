package com.example.demo.repository;

import com.example.demo.model.DeviceToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * device_tokens koleksiyonu için erişim. Aynı deviceId ile save() çağrıldığında doküman güncellenir (upsert).
 */
@Repository
public interface IDeviceTokenRepository extends MongoRepository<DeviceToken, String> {
}
