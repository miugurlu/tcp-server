package com.example.demo.repository;

import com.example.demo.model.DeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DeviceLog kayıtları için veritabanı erişim arayüzü. JpaRepository, Spring Data JPA modülünden gelir;
 * veritabanıyla etkileşimi (kaydet, bul, sil, listele vb.) sadeleştirir.
 * Bu arayüzü kullanmak için JpaRepository'den extend edip hangi entity (DeviceLog) ve hangi ID tipinin (Long)
 * kullanılacağını generic parametrelerle belirtiyoruz. Spring, bu arayüz için implementasyonu çalışma anında üretir.
 */
@Repository
public interface IDeviceRepository extends JpaRepository<DeviceLog, Long> {
    // JpaRepository sayesinde save() gibi metodlar otomatik gelir.
}