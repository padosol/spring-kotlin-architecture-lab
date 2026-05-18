# Design Pattern Practice

Kotlin, Spring Boot, Gradle Kotlin DSL 기반 디자인 패턴 연습 프로젝트입니다.

## Stack

- Kotlin 2.2.21
- Spring Boot 4.0.6
- Java 21
- Gradle Kotlin DSL
- Spring Web MVC, Spring Security, Validation, Spring Data JPA, H2

## Practice Areas

- `payment`: `PaymentProcessor` 전략을 통해 결제 방식별 처리 구현
- `notification`: `NotificationSender` 전략을 통해 채널별 알림 발송 구현
- `auth`: `Authenticator` 전략을 통해 인증 방식별 로그인 구현

## Roadmap

- [TODO: Design Pattern Practice Roadmap](docs/TODO.md)
- [Learning Log](docs/LEARNING_LOG.md)

## Run

```bash
./gradlew bootRun
```

## Test

```bash
./gradlew test
```

## Sample Requests

```bash
curl -X POST http://localhost:8080/api/payments \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"order-1","amount":10000,"method":"CARD"}'
```

```bash
curl -X POST http://localhost:8080/api/notifications \
  -H 'Content-Type: application/json' \
  -d '{"recipient":"user@example.com","title":"Hello","message":"Welcome","channel":"EMAIL"}'
```

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"principal":"user@example.com","credential":"secret","type":"PASSWORD"}'
```
