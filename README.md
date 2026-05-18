# Spring Kotlin Architecture Lab

Kotlin, Spring Boot, Gradle Kotlin DSL 기반 e-commerce 아키텍처 학습 프로젝트입니다.

주문 흐름 안에서 `payment`, `notification`, `auth`를 단계적으로 개선하며 TDD, 레이어 분리, DDD, 디자인 패턴 적용 전후를 비교합니다.

## Stack

- Kotlin 2.2.21
- Spring Boot 4.0.6
- Java 21
- Gradle Kotlin DSL
- Spring Web MVC, Spring Security, Validation, Spring Data JPA, H2

## Practice Areas

- `payment`: 주문 결제 승인, 실패, 취소, 환불 흐름을 `PaymentProcessor` 전략으로 확장
- `notification`: 결제 완료, 결제 실패, 로그인 보안 알림을 `NotificationSender` 전략으로 확장
- `auth`: 고객 인증, 계정 상태, 토큰 발급 흐름을 `Authenticator` 전략으로 확장
- 주변 도메인: `order`, `cart`, `catalog`, `inventory`, `shipment`는 초기에는 문서상 경계로만 다루고 이후 단계에서 필요할 때 확장

## Roadmap

- [TODO: E-commerce Architecture Practice Roadmap](docs/TODO.md)
- [Roadmap Details](docs/roadmap/README.md)
- [E-commerce Domain Baseline](docs/roadmap/step-00-5-domain-baseline.md)
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
