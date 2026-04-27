# Local API (Spring Boot REST API)

## 🎯 Цель проекта
Проект создан для практики разработки REST API и подготовки к API автоматизации тестирования (RestAssured).

## 📌 Описание проекта
Проект представляет собой REST API, разработанный с использованием Spring Boot.

Реализована многослойная архитектура (Controller → Service → Repository) и работа с базой данных PostgreSQL через Spring Data JPA.

## 🧠 Что реализовано
- Разработан REST API на Spring Boot
- Реализована слоистая архитектура (Controller, Service, Repository)
- Использован паттерн DTO (Request / Response)
- Настроена работа с PostgreSQL через Spring Data JPA
- Маппинг JSON → Java объекты через @RequestBody
- Возврат JSON ответов через @RestController
- Реализован DTO mapping между слоями
- Разделение бизнес-логики и HTTP слоя (Controller / Service)

## 🏗 Архитектура

```
Клиент (JSON)
↓
Controller (REST слой)
↓
Service (бизнес-логика)
↓
Repository (работа с БД)
↓
PostgreSQL
```

## 🔥 Основной endpoint
### Создание пользователя
POST /users

### Запрос:
```
{
"email": "test@mail.com",
"password": "1234"
}
```

### Ответ:
```
{
  "id": 1,
  "email": "test@mail.com"
}
```

## 🧱 Технологии
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## 🚀 Как запустить
1. Клонировать репозиторий
2. Настроить PostgreSQL в application.yml
3. Запустить приложение
4. API доступно по адресу: http://localhost:8080

## 📌 Дальнейшее развитие проекта
- Добавить валидацию (@Valid)
- Добавить обработку ошибок (Exception Handler)
- Подключить Swagger
- Добавить Spring Security (авторизация)
- Написать API автотесты (RestAssured)
- Docker контейнеризация