# Device Activity Backend

Bu proje, iOS üzerindeki `ilkelMDM` uygulamasından gelen cihaz aktivitelerini toplayan, MongoDB'ye kaydeden ve gün sonunda raporlayan bir Spring Boot backend servisidir.

- Cihazdan gelen loglar MongoDB'ye kaydedilir.
- Uygulama kapalıyken **silent push notification** ile cihazdan veri alınmaya devam edilir.
- Gün sonunda toplanan verilerle bir gün sonu raporu oluşturulur ve mail olarak gönderilir.

---

## Mimari genel bakış

- **Backend**: Spring Boot (Java 17)
- **Veritabanı**: MongoDB
- **Bildirim**: Apple Push Notification Service (APNS) üzerinden silent push
- **Raporlama**: Gün sonu e-posta raporu (Thymeleaf template + Spring Mail)

Akış:

1. Backend ayağa kalkar ve MongoDB'ye bağlanır.
2. Fiziksel iOS cihazda çalışan `tcp-server` uygulaması, cihaz loglarını backend’e gönderir.
3. Backend bu logları MongoDB'ye kaydeder.
4. Gün içerisinde cihaz kapalı olsa bile, gönderilen silent push bildirimleri sayesinde cihazdan periyodik veri toplanır.
5. Gün sonunda toplanan verilerle günlük rapor üretilir ve e-posta olarak iletilir.

---

## Gereksinimler

- Java 17
- Maven
- MongoDB
- Ngrok

---

## Konfigürasyon

Örnek konfigürasyon dosyası: `src/main/resources/application.example.yml`

---

## Çalıştırma

### 1. MongoDB’yi başlat

Varsayılan bağlantı: `mongodb://localhost:27017`

---

### 2. Uygulama konfigürasyonunu hazırla

```bash
cp src/main/resources/application.example.yml src/main/resources/application.yml
# application.yml içindeki mail, mongodb, apns ve port gibi ayarları kendine göre düzenle
```

---

### 3. Backend’i çalıştır

Maven ile:

```bash
mvn spring-boot:run
```

veya Maven wrapper ile:

```bash
./mvnw spring-boot:run
```

Uygulama varsayılan olarak `http://localhost:9090` üzerinde dinler.

## Uçtan uca akış

1. Backend çalışır ve MongoDB’ye bağlıdır.
2. Ngrok üzerinden backend portu dış dünyaya açılır.
3. Xcode üzerinden iOS istemci projesi, fiziksel cihazda ngrok adresi ile çalıştırılır.
4. Cihazdan gelen loglar backend’e gönderilir ve MongoDB’ye kaydedilir.
5. Gün içinde silent push bildirimleri ile cihazdan periyodik veri alınır.
6. Gün sonunda backend, o güne ait verilerle bir rapor oluşturur ve e-posta ile gönderir.

---

## İlgili projeler

- iOS istemci (TCP client): [ilkeMDM](https://github.com/miugurlu/ilkelMDM.git)
