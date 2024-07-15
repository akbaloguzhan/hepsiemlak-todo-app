# HepsiEmlak Todo App Backend
Bu proje Spring Boot, Java 21 kullanılarak geliştirilmiş bir Todo App backendidir.

## Özellikler
- Kullanıcı kayıt ve giriş
- Todo Itemları için CRUD işlemleri
- Kullanıcıya özel Todo Itemları
- RESTful API
- Couchbase veritabanı entegrasyonu
- Spring Security ile güvenlik işlemleri

## Gereksinimler
- Java 21
- Maven
- Couchbase Server

## Kurulum
1. Projeyi klonlayın
2. cd ile proje dizinine gidin
3. Couchbase Server'ı kurun ve çalıştırın
4. 'application.properties' dosyasını güncelleyerek kendi credentials (username, password) bilgilerinizi girin
5. mvn clean install komutu ile dependency'leri dahil edin
6. Uygulamayı çalıştırın

## DockerHUB Linki
https://hub.docker.com/repository/docker/akbaloguzhan/todo-app

## API kullanımı
API dokümantasyonuna http://localhost:8080/swagger-ui.html adresinden erişebilirsiniz

## Testler
mvn test komutu ile projedeki testleri çalıştırabilir ve çıktıları alabilirsiniz.

## İletişim
Oğuzhan AKBAL - ogzhn.akbal@gmail.com - linkedin.com/in/akbaloguzhan
