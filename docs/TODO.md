# TODO: E-commerce Architecture Practice Roadmap

e-commerce 도메인의 주문 흐름 안에서 `payment`, `notification`, `auth` 세 영역을 단계적으로 개선하면서 TDD, 레이어 분리, DDD, 디자인 패턴을 학습하기 위한 진행 체크리스트다.

상세한 `As-Is`, `To-Be`, 변경 기록 포인트, 학습 포인트, 리뷰 질문은 [Roadmap Details](roadmap/README.md)에 분리해서 관리한다. 단계별 회고는 [Learning Log](LEARNING_LOG.md)에 누적한다.

## Current Baseline

### As-Is

- `payment`, `notification`, `auth` 각각이 하나의 Kotlin 파일에 Controller, Request/Response DTO, Service, Interface, 구현체를 함께 가진다.
- 세 도메인은 e-commerce의 주문, 결제, 고객 커뮤니케이션, 고객 인증 흐름에 연결되는 규칙과 확장 후보를 문서로 가진다.
- `order`, `cart`, `catalog`, `inventory`, `shipment`는 아직 코드에 없고 주변 도메인으로만 문서화되어 있다.
- 세 도메인의 주요 규칙은 아직 코드에는 반영되어 있지 않다.
- DB 의존성은 준비되어 있지만 아직 영속성 모델, Repository, 트랜잭션 경계가 없다.
- 테스트는 Spring context 로딩과 API smoke test 중심이다.
- `PaymentProcessor`, `NotificationSender`, `Authenticator`로 기본적인 Strategy 패턴만 연습 가능한 상태다.

### To-Be

- HTTP/API, Application, Domain, Infrastructure 레이어를 분리한다.
- 도메인 규칙은 Controller나 DTO가 아니라 Domain/Application 계층에 둔다.
- 주문 흐름을 기준으로 결제 승인, 결제 실패, 알림 발송, 인증 실패 정책을 테스트로 고정한다.
- 테스트를 먼저 작성하고 실패를 확인한 뒤 구현하는 TDD 루프를 따른다.
- 디자인 패턴은 목적이 분명할 때만 적용하고, 적용 전후의 장단점을 문서화한다.
- 새 결제 수단, 새 알림 채널, 새 인증 방식을 기존 코드 변경 최소화로 추가할 수 있게 만든다.

## Progress

- [x] Step 0. Project Bootstrap: Spring Boot/Kotlin 프로젝트 생성, 샘플 API, Git 초기화
- [x] [Step 0.5. E-commerce 도메인 베이스라인 정의](roadmap/step-00-5-domain-baseline.md)
- [ ] [Step 1. TDD 안전망 만들기](roadmap/step-01-tdd-safety-net.md)
- [ ] [Step 2. 레이어 분리](roadmap/step-02-layer-separation.md)
- [ ] [Step 3. DDD 기반 도메인 모델링](roadmap/step-03-ddd-domain-modeling.md)
- [ ] [Step 4. 디자인 패턴을 목적별로 적용](roadmap/step-04-design-patterns.md)
- [ ] [Step 5. 영속성과 트랜잭션 경계 추가](roadmap/step-05-persistence-transaction.md)
- [ ] [Step 6. 에러 처리와 API 계약 정리](roadmap/step-06-error-api-contract.md)
- [ ] [Step 7. 확장성, 유지보수성, 재사용성 강화](roadmap/step-07-extensibility-maintainability-reuse.md)
- [ ] [Step 8. 실전 품질 개선](roadmap/step-08-operational-quality.md)

## Step Checklist

### Step 0.5. E-commerce 도메인 베이스라인 정의

- [x] e-commerce 학습 범위와 주변 도메인 정의
- [x] 대표 주문, 결제, 알림, 인증 흐름 정의
- [x] `payment` 주요 개념, 핵심 규칙, 확장 후보 정의
- [x] `notification` 주요 개념, 핵심 규칙, 확장 후보 정의
- [x] `auth` 주요 개념, 핵심 규칙, 확장 후보 정의
- [x] 현재 코드 베이스라인 스냅샷 기록
- [x] Step 0.5에서 코드 변경을 하지 않는 이유 기록
- [x] 의도적으로 남길 아키텍처 약점 정의
- [x] Step 1 테스트 후보 도출
- [x] Step 1에서 먼저 다룰 테스트 범위 우선순위화

### Step 1. TDD 안전망 만들기

- [ ] 서비스 단위 테스트 추가
- [ ] 지원 타입별 성공 케이스 검증
- [ ] 미지원 타입 실패 정책 테스트로 고정
- [ ] validation 실패 API 테스트 추가
- [ ] Step 0.5의 e-commerce 규칙 중 우선순위 높은 실패 케이스 테스트 추가
- [ ] 주문 결제, 결제 완료 알림, 고객 인증 흐름의 최소 happy path 고정
- [ ] `./gradlew test` 통과

### Step 2. 레이어 분리

- [ ] API, Application, Domain, Infrastructure 패키지 분리
- [ ] Controller는 Application Service만 호출
- [ ] Application Service에서 HTTP 타입 제거
- [ ] Domain에서 Spring MVC annotation 제거
- [ ] 기존 API smoke test 통과

### Step 3. DDD 기반 도메인 모델링

- [ ] `Money`, `OrderId`, `TransactionId` Value Object 추가
- [ ] `Payment` Aggregate 추가
- [ ] `NotificationMessage`, `Recipient` Value Object 추가
- [ ] `AuthPrincipal`, `Credential` Value Object 추가
- [ ] Repository port 정의
- [ ] 도메인 테스트를 Spring context 없이 실행

### Step 4. 디자인 패턴을 목적별로 적용

- [ ] Strategy 선택 로직을 registry/factory 구조로 정리
- [ ] payment 승인 흐름에 Chain of Responsibility 또는 Template Method 적용
- [ ] notification 외부 연동에 Adapter 적용
- [ ] notification 발송에 Decorator로 retry/logging 추가
- [ ] auth 정책 검증에 Specification 적용
- [ ] 패턴 적용 전후 비교 기록

### Step 5. 영속성과 트랜잭션 경계 추가

- [ ] `PaymentEntity`, `PaymentJpaRepository` 추가
- [ ] Repository port와 JPA adapter 구현
- [ ] 결제/알림/인증 이력 저장
- [ ] Application Service에 트랜잭션 경계 설정
- [ ] JPA slice test 또는 통합 테스트 추가

### Step 6. 에러 처리와 API 계약 정리

- [ ] `GlobalExceptionHandler` 추가
- [ ] `ErrorResponse` 표준화
- [ ] 도메인 예외와 HTTP 응답 변환 분리
- [ ] validation 오류와 비즈니스 오류 테스트 추가
- [ ] OpenAPI 문서 도입 여부 검토

### Step 7. 확장성, 유지보수성, 재사용성 강화

- [ ] 공통 `StrategyRegistry<K, V>` 추출 가능성 검토
- [ ] 도메인 이벤트 추가
- [ ] 이벤트 기반 알림 발송 흐름 구현
- [ ] 레이어 의존성 규칙 테스트 검토
- [ ] 공통화할 코드와 공통화하지 않을 도메인 규칙 문서화

### Step 8. 실전 품질 개선

- [ ] structured logging 적용
- [ ] 외부 adapter timeout/retry 정책 추가
- [ ] Spring Security 인증 흐름을 auth 도메인과 연결
- [ ] Testcontainers 도입 검토
- [ ] CI에서 `./gradlew test` 실행
- [ ] 성능/동시성 시나리오 정의

## Working Rules

- 한 단계에서 하나의 학습 주제만 크게 바꾼다.
- 각 단계는 상세 문서의 `As-Is`, `To-Be`, `Done Criteria`를 먼저 확인한다.
- 먼저 테스트로 현재 동작을 고정한 뒤 구조를 바꾼다.
- 리팩터링 단계에서는 외부 API 계약을 가능하면 유지한다.
- 패턴 이름보다 의존성 방향, 변경 범위, 테스트 용이성을 우선한다.
- 단계 완료 후 [Learning Log](LEARNING_LOG.md)에 회고를 추가한다.
