# TODO: Design Pattern Practice Roadmap

이 문서는 `payment`, `notification`, `auth` 세 도메인을 단계적으로 개선하면서 TDD, 레이어 분리, DDD, 디자인 패턴을 학습하기 위한 로드맵이다.

목표는 단순히 패턴을 많이 적용하는 것이 아니라, 확장성, 유지보수성, 재사용성이 좋아지는 이유를 코드 변화로 확인하는 것이다.

## 현재 기준

### As-Is

- `payment`, `notification`, `auth` 각각이 하나의 Kotlin 파일에 Controller, Request/Response DTO, Service, Interface, 구현체를 함께 가진다.
- DB 의존성은 준비되어 있지만 아직 영속성 모델, Repository, 트랜잭션 경계가 없다.
- 모든 처리는 성공 응답 중심의 샘플 로직이다.
- 테스트는 Spring context 로딩과 API smoke test 중심이다.
- `PaymentProcessor`, `NotificationSender`, `Authenticator`로 기본적인 Strategy 패턴만 연습 가능한 상태다.

### To-Be

- HTTP/API, Application, Domain, Infrastructure 레이어를 분리한다.
- 도메인 규칙은 Controller나 DTO가 아니라 Domain/Application 계층에 둔다.
- 테스트를 먼저 작성하고 실패를 확인한 뒤 구현하는 TDD 루프를 따른다.
- 디자인 패턴은 목적이 분명할 때만 적용하고, 적용 전후의 장단점을 문서화한다.
- 새 결제 수단, 새 알림 채널, 새 인증 방식을 기존 코드 변경 최소화로 추가할 수 있게 만든다.

## 진행 원칙

- 한 단계에서 하나의 학습 주제만 크게 바꾼다.
- 각 단계는 `as-is`, `to-be`, `done criteria`가 명확해야 한다.
- 먼저 테스트로 현재 동작을 고정한 뒤 구조를 바꾼다.
- 리팩터링 단계에서는 외부 API 계약을 가능하면 유지한다.
- 패턴 이름보다 의존성 방향, 변경 범위, 테스트 용이성을 우선한다.

## 학습 기록 방식

각 단계는 코드 변경만 남기지 않고, 변경 전후의 설계 판단을 함께 기록한다. 목표는 나중에 커밋 이력과 문서를 함께 보면서 "왜 이 구조로 바꿨는지", "어떤 문제가 줄었는지", "다음에는 무엇을 개선해야 하는지"를 복기할 수 있게 만드는 것이다.

### 단계 시작 전 기록

- `As-Is`: 현재 코드의 책임 배치, 의존성 방향, 테스트 상태, 변경 비용을 적는다.
- `문제 정의`: 지금 구조에서 학습하거나 개선하려는 불편함을 한두 문장으로 적는다.
- `To-Be`: 이번 단계가 끝났을 때 코드가 어떤 형태가 되어야 하는지 적는다.
- `검증 방법`: 어떤 테스트, 실행 명령, 코드 리뷰 기준으로 완료 여부를 확인할지 적는다.

### 단계 진행 중 기록

- 실패한 테스트 또는 깨진 설계를 먼저 기록한다.
- 선택한 설계 대안과 선택하지 않은 대안을 함께 적는다.
- 패턴을 적용할 때는 패턴 이름보다 "변경 지점", "의존성 방향", "테스트 용이성"을 먼저 적는다.
- 커밋은 학습 단위가 보이도록 작게 나눈다.

### 단계 완료 후 리뷰

- `What changed`: 실제로 바뀐 파일, 패키지, 의존성, 테스트를 요약한다.
- `What improved`: 확장성, 유지보수성, 재사용성, 테스트 용이성 중 좋아진 부분을 적는다.
- `Trade-off`: 코드가 늘어난 부분, 복잡해진 부분, 아직 과한 추상화로 보이는 부분을 적는다.
- `Learning point`: 이번 단계에서 배운 설계 원칙, 패턴 적용 기준, Spring/Kotlin 사용법을 적는다.
- `Next action`: 다음 단계에서 이어서 검증하거나 정리할 항목을 적는다.

### 단계별 기록 카드

각 단계가 끝날 때 [LEARNING_LOG.md](LEARNING_LOG.md)에 아래 형식으로 기록한다.

```markdown
## Step N. 제목

### Date

### Commit

### As-Is

### Problem

### To-Be

### Changes

### Tests

### Learning Points

### Trade-Offs

### Next Actions
```

## Step 1. TDD 안전망 만들기

### As-Is

- API smoke test는 있지만 서비스 단위 테스트가 부족하다.
- 실패 케이스, 경계값, 지원하지 않는 타입에 대한 테스트가 부족하다.
- 구현을 먼저 만들고 테스트로 확인하는 흐름이다.

### To-Be

- Service 단위 테스트를 먼저 작성한다.
- 지원 타입별 성공 케이스와 미지원 타입 실패 케이스를 테스트한다.
- validation 실패는 Controller/API 테스트에서 확인한다.
- 새 기능은 `Red -> Green -> Refactor` 순서로 진행한다.

### TODO

- [ ] `PaymentServiceTest` 추가: `CARD`, `BANK_TRANSFER` 성공 케이스 검증
- [ ] `NotificationServiceTest` 추가: `EMAIL`, `SMS`, `PUSH` 성공 케이스 검증
- [ ] `AuthServiceTest` 추가: `PASSWORD`, `OAUTH` 성공 케이스 검증
- [ ] 미지원 타입 처리 정책을 정하고 테스트로 고정
- [ ] 요청 필드 누락, 빈 문자열, 음수 금액 validation 테스트 추가
- [ ] 테스트 이름을 요구사항 문장처럼 작성

### 변경 기록 포인트

- 테스트를 추가하기 전 현재 서비스의 책임과 실패 정책을 기록한다.
- 첫 실패 테스트가 어떤 요구사항을 표현하는지 남긴다.
- 테스트를 통과시키기 위해 최소 구현만 했는지 확인한다.

### 학습 포인트

- TDD의 `Red -> Green -> Refactor` 흐름을 실제 커밋 단위로 경험한다.
- 단위 테스트와 API 테스트의 책임 차이를 구분한다.
- 실패 케이스를 먼저 고정하면 리팩터링이 쉬워지는 이유를 확인한다.

### 리뷰 질문

- 테스트가 구현 세부사항이 아니라 외부에서 기대하는 동작을 검증하는가?
- 새 전략 구현체를 추가할 때 기존 테스트가 의도한 안전망으로 동작하는가?
- 실패 케이스의 정책이 테스트 이름만 봐도 이해되는가?

### Done Criteria

- `./gradlew test`가 통과한다.
- 각 도메인마다 성공/실패 테스트가 최소 1개 이상 있다.
- 새 전략 구현체를 추가하기 전에 실패하는 테스트를 먼저 작성할 수 있다.

## Step 2. 레이어 분리

### As-Is

- Controller 파일 하나에 여러 책임이 모여 있다.
- DTO와 도메인 모델이 구분되지 않는다.
- Service가 전략 선택과 유스케이스 실행을 동시에 담당한다.

### To-Be

- API 레이어는 HTTP 요청/응답과 validation만 담당한다.
- Application 레이어는 유스케이스와 트랜잭션 경계를 담당한다.
- Domain 레이어는 핵심 규칙과 모델을 담당한다.
- Infrastructure 레이어는 외부 시스템, DB, SDK 연동을 담당한다.

### 권장 패키지 구조

```text
com.example.designpattern
├── payment
│   ├── api
│   ├── application
│   ├── domain
│   └── infrastructure
├── notification
│   ├── api
│   ├── application
│   ├── domain
│   └── infrastructure
└── auth
    ├── api
    ├── application
    ├── domain
    └── infrastructure
```

### TODO

- [ ] `PaymentController`를 `payment.api`로 이동
- [ ] `PaymentService`를 `payment.application`으로 이동
- [ ] `PaymentProcessor`, `PaymentMethod`, `PaymentStatus`를 `payment.domain`으로 이동
- [ ] 카드/계좌이체 구현체를 `payment.infrastructure` 또는 `payment.domain.strategy`로 분리
- [ ] `notification`, `auth`도 같은 기준으로 분리
- [ ] API DTO와 도메인 Command/Result 객체 분리

### 변경 기록 포인트

- 이동 전 패키지 구조와 이동 후 패키지 구조를 비교한다.
- Controller, Application Service, Domain, Infrastructure가 각각 무엇을 모르게 되었는지 기록한다.
- 이동만 한 변경과 책임을 바꾼 변경을 구분해서 커밋한다.

### 학습 포인트

- 레이어 분리는 폴더 정리가 아니라 의존성 방향을 고정하는 작업임을 확인한다.
- HTTP DTO와 유스케이스 Command를 분리했을 때 테스트와 변경 범위가 어떻게 달라지는지 본다.
- Spring annotation이 도메인 모델로 새지 않게 만드는 기준을 세운다.

### 리뷰 질문

- Controller가 비즈니스 규칙을 판단하고 있지 않은가?
- Application Service가 HTTP request/response 타입을 직접 알고 있지 않은가?
- Domain 코드는 Spring 없이 테스트할 수 있는가?

### Done Criteria

- Controller는 Application Service만 호출한다.
- Application Service는 HTTP 타입을 모른다.
- Domain 객체는 Spring MVC annotation을 모른다.
- 기존 API smoke test가 그대로 통과한다.

## Step 3. DDD 기반 도메인 모델링

### As-Is

- `orderId`, `principal`, `recipient`가 단순 문자열이다.
- 금액, 인증 정보, 알림 대상 같은 개념의 불변식이 흩어질 가능성이 크다.
- 아직 Aggregate, Value Object, Domain Service의 기준이 없다.

### To-Be

- 중요한 값은 Value Object로 감싼다.
- 도메인 규칙은 생성자나 팩토리에서 보장한다.
- 유스케이스별 Command와 Result를 명시한다.
- Repository는 인터페이스를 Domain/Application 쪽에 두고 구현은 Infrastructure에 둔다.

### TODO

- [ ] `Money`, `OrderId`, `TransactionId` Value Object 추가
- [ ] `Payment` Aggregate 추가: 결제 요청, 승인, 실패 상태 관리
- [ ] `NotificationMessage`, `Recipient` Value Object 추가
- [ ] `AuthPrincipal`, `Credential` Value Object 추가
- [ ] 도메인 객체 생성 실패 케이스 테스트 추가
- [ ] Repository port 정의: `PaymentRepository`, `NotificationHistoryRepository`, `UserCredentialRepository`

### 변경 기록 포인트

- primitive 값이 Value Object로 바뀌면서 어느 규칙이 한곳으로 모였는지 기록한다.
- Aggregate를 도입한 이유와 Aggregate에 넣지 않은 책임을 함께 적는다.
- Repository port의 위치와 Infrastructure 구현체의 의존성 방향을 기록한다.

### 학습 포인트

- Value Object가 validation 중복과 잘못된 상태 생성을 어떻게 줄이는지 확인한다.
- Aggregate가 상태 변경의 진입점을 제한하는 방식을 경험한다.
- DDD를 JPA Entity 설계와 동일시하지 않고 도메인 규칙 중심으로 바라본다.

### 리뷰 질문

- 잘못된 금액, 빈 식별자, 잘못된 인증 정보가 객체 생성 시점에 막히는가?
- 도메인 객체가 DB 저장 방식이나 HTTP 표현 방식에 묶여 있지 않은가?
- Aggregate가 너무 많은 책임을 가지거나 반대로 의미 없는 wrapper가 되지는 않았는가?

### Done Criteria

- 잘못된 도메인 상태를 만들기 어렵다.
- primitive obsession이 줄어든다.
- 도메인 테스트는 Spring context 없이 빠르게 실행된다.

## Step 4. 디자인 패턴을 목적별로 적용

### As-Is

- Strategy 패턴만 얇게 적용되어 있다.
- 패턴 적용 이유와 대체 설계가 문서화되어 있지 않다.
- 구현체 선택 로직이 `firstOrNull { supports(...) }` 형태로 반복된다.

### To-Be

- 변경 가능성이 높은 지점에 패턴을 적용한다.
- 패턴 적용 전후의 변경 범위와 테스트 난이도를 비교한다.
- 공통 선택 로직은 재사용 가능한 구조로 이동한다.

### 도메인별 후보 패턴

| Domain | Pattern | 적용 목적 |
| --- | --- | --- |
| payment | Strategy | 결제 수단별 처리 로직 교체 |
| payment | Factory Method | 결제 수단에 맞는 processor 선택 책임 분리 |
| payment | Chain of Responsibility | 할인, 검증, 한도 확인, 승인 요청을 순차 처리 |
| payment | Template Method | 결제 처리 공통 흐름과 세부 구현 분리 |
| notification | Adapter | 이메일/SMS/Push 외부 SDK 호출 방식 통일 |
| notification | Observer | 결제 완료, 로그인 성공 같은 이벤트에 반응 |
| notification | Decorator | 재시도, 로깅, rate limit, tracing 기능 추가 |
| auth | Strategy | 인증 방식별 인증 로직 교체 |
| auth | Chain of Responsibility | 토큰, 세션, OAuth, API key 인증 순차 시도 |
| auth | Specification | 비밀번호 정책, 계정 잠금, 권한 조건 조합 |
| auth | Proxy | 인증된 사용자만 특정 기능 접근 허용 |

### TODO

- [ ] Strategy 선택 로직을 `ProcessorRegistry` 형태로 추출
- [ ] payment 승인 흐름에 Chain of Responsibility 적용
- [ ] notification 외부 발송 모듈에 Adapter 적용
- [ ] notification 발송에 Decorator로 retry/logging 추가
- [ ] auth 정책 검증에 Specification 적용
- [ ] 각 패턴별 `before.md`, `after.md` 또는 README 섹션 작성

### 변경 기록 포인트

- 패턴 적용 전 변경 비용과 적용 후 변경 비용을 비교한다.
- 패턴을 적용한 이유, 선택하지 않은 대안, 제거해도 되는 조건을 기록한다.
- 패턴별로 테스트가 단순해졌는지 복잡해졌는지 확인한다.

### 학습 포인트

- Strategy, Factory, Adapter, Decorator, Chain of Responsibility, Specification의 적용 기준을 코드로 비교한다.
- 패턴이 목적 없이 늘어나면 유지보수성이 오히려 떨어질 수 있음을 확인한다.
- 확장 지점과 안정 지점을 나누어 설계하는 습관을 만든다.

### 리뷰 질문

- 새 구현체를 추가할 때 기존 유스케이스 코드 변경이 줄었는가?
- 패턴 이름을 모르는 사람이 봐도 흐름을 이해할 수 있는가?
- 이 추상화가 현재 요구사항 대비 너무 이른 추상화는 아닌가?

### Done Criteria

- 새 구현체 추가 시 기존 유스케이스 코드 변경이 최소화된다.
- 패턴을 제거했을 때보다 코드가 읽기 쉽거나 테스트하기 쉬운 이유가 설명된다.
- 패턴 적용 후에도 테스트가 빠르고 명확하다.

## Step 5. 영속성과 트랜잭션 경계 추가

### As-Is

- H2와 JPA 의존성은 있지만 Entity와 Repository가 없다.
- 결제/알림/인증 이력이 저장되지 않는다.
- 트랜잭션 경계가 없다.

### To-Be

- Application Service가 트랜잭션 경계를 가진다.
- Domain 모델과 JPA Entity를 직접 합칠지 분리할지 실험하고 비교한다.
- 저장소 구현은 port/adapter 구조로 분리한다.

### TODO

- [ ] `PaymentEntity`, `PaymentJpaRepository` 추가
- [ ] `PaymentRepository` port와 JPA adapter 구현
- [ ] 결제 요청/승인 결과 저장
- [ ] 알림 발송 이력 저장
- [ ] 로그인 성공/실패 이력 저장
- [ ] `@DataJpaTest` 또는 Boot 4 대응 JPA slice test 추가

### 변경 기록 포인트

- 트랜잭션 경계를 어디에 두었는지, 왜 그 위치가 적절한지 기록한다.
- Domain 모델과 JPA Entity를 합쳤는지 분리했는지, 그 이유를 남긴다.
- 실패 시 저장 정책과 롤백 정책을 테스트와 함께 기록한다.

### 학습 포인트

- Application Service가 유스케이스와 트랜잭션의 경계가 되는 이유를 이해한다.
- Repository port/adapter 구조가 테스트 대역과 인프라 교체를 어떻게 쉽게 만드는지 확인한다.
- JPA 편의성과 도메인 순수성 사이의 현실적인 균형을 경험한다.

### 리뷰 질문

- Controller나 Domain이 JPA Repository를 직접 알지 않는가?
- 저장 실패, 외부 호출 실패, 부분 성공 상황의 정책이 명확한가?
- 테스트에서 실제 DB가 필요한 경우와 fake로 충분한 경우를 구분했는가?

### Done Criteria

- Application Service 테스트에서 Repository를 fake로 대체할 수 있다.
- JPA 세부사항이 Controller까지 새지 않는다.
- 실패 시 저장 정책이 테스트로 설명된다.

## Step 6. 에러 처리와 API 계약 정리

### As-Is

- 일부 오류는 `ResponseStatusException`에 의존한다.
- 공통 오류 응답 포맷이 없다.
- API 계약 문서가 README의 curl 예제 수준이다.

### To-Be

- 도메인 예외와 API 오류 응답을 분리한다.
- 공통 error response를 정의한다.
- validation 오류, 미지원 타입, 인증 실패, 외부 연동 실패를 구분한다.

### TODO

- [ ] `GlobalExceptionHandler` 추가
- [ ] `ErrorResponse` 표준화
- [ ] 도메인 예외 추가: `UnsupportedPaymentMethodException`, `AuthenticationFailedException` 등
- [ ] API 오류 테스트 추가
- [ ] OpenAPI 문서 도입 여부 검토

### 변경 기록 포인트

- 도메인 예외와 API 응답 변환 지점을 분리한 방식을 기록한다.
- 오류 코드, 메시지, HTTP status를 정한 기준을 남긴다.
- validation 오류와 비즈니스 오류를 테스트에서 어떻게 구분했는지 기록한다.

### 학습 포인트

- 예외는 던지는 위치보다 해석하고 변환하는 경계가 중요하다는 점을 확인한다.
- API 계약을 표준화하면 클라이언트와 테스트가 안정되는 이유를 경험한다.
- 도메인 예외가 HTTP에 직접 의존하지 않도록 만드는 방식을 익힌다.

### 리뷰 질문

- 같은 종류의 오류가 항상 같은 JSON 구조로 내려가는가?
- 클라이언트가 재시도 가능 오류와 입력 오류를 구분할 수 있는가?
- 오류 응답 포맷 변경 시 테스트가 계약 변경을 감지하는가?

### Done Criteria

- 클라이언트가 오류 원인을 일관된 JSON 형식으로 받을 수 있다.
- 도메인 예외가 HTTP 상태 코드에 직접 의존하지 않는다.

## Step 7. 확장성, 유지보수성, 재사용성 강화

### As-Is

- 도메인별로 비슷한 전략 선택 코드가 반복될 수 있다.
- 모듈 경계가 약해서 서로 쉽게 의존할 수 있다.
- 공통 기능을 어디에 둘지 기준이 없다.

### To-Be

- 재사용 가능한 registry, command handler, event publisher 구조를 만든다.
- 공통 코드는 `shared` 또는 `common`에 두되 도메인 규칙은 공통화하지 않는다.
- 도메인 간 통신은 직접 호출보다 이벤트를 우선 검토한다.

### TODO

- [ ] 공통 `StrategyRegistry<K, V>` 추출 가능성 검토
- [ ] 도메인 이벤트 추가: `PaymentApprovedEvent`, `LoginSucceededEvent`
- [ ] 이벤트 기반 알림 발송 흐름 구현
- [ ] ArchUnit 또는 유사 도구로 레이어 의존성 규칙 검토
- [ ] 공통 모듈로 빼도 되는 코드와 빼면 안 되는 도메인 규칙 구분 문서화

### 변경 기록 포인트

- 공통화한 코드와 공통화하지 않은 도메인 규칙을 나누어 기록한다.
- 이벤트 기반 흐름으로 바뀌면서 직접 의존성이 어떻게 줄었는지 기록한다.
- 레이어 의존성 규칙을 코드나 테스트로 어떻게 검증했는지 남긴다.

### 학습 포인트

- 재사용성은 중복 제거가 아니라 안정적인 책임을 추출하는 일임을 확인한다.
- 도메인 이벤트가 결합도를 낮추는 지점과 디버깅을 어렵게 만드는 지점을 함께 경험한다.
- 아키텍처 규칙을 문서가 아니라 테스트로 강제하는 방법을 학습한다.

### 리뷰 질문

- 공통 모듈이 도메인 의미를 흐리게 만들지는 않았는가?
- 이벤트 발행자와 구독자의 실패 정책이 명확한가?
- 새 도메인을 추가할 때 복사 붙여넣기보다 재사용 가능한 구조가 있는가?

### Done Criteria

- payment가 notification 구현체를 직접 알지 않는다.
- 새 도메인을 추가해도 기존 도메인의 내부 구조를 복사하지 않아도 된다.
- 공통화가 과해져 도메인 의미가 흐려지지 않는다.

## Step 8. 실전 품질 개선

### As-Is

- 로깅, 관측성, 보안 정책, 운영 설정이 최소 수준이다.
- 실제 외부 연동 실패, 재시도, timeout 정책이 없다.
- 성능이나 동시성 요구사항이 정의되어 있지 않다.

### To-Be

- 외부 연동에는 timeout, retry, fallback 정책을 둔다.
- 보안은 학습용 permit-all에서 실제 인증/인가 흐름으로 발전시킨다.
- 테스트 피라미드를 갖춘다: unit, slice, integration, API contract.

### TODO

- [ ] structured logging 적용
- [ ] 외부 adapter timeout/retry 정책 추가
- [ ] Spring Security 인증 흐름을 실제 auth 도메인과 연결
- [ ] Testcontainers 도입 검토
- [ ] CI에서 `./gradlew test` 실행
- [ ] 성능/동시성 시나리오 정의

### 변경 기록 포인트

- 운영 설정과 학습용 설정을 어떻게 분리했는지 기록한다.
- 외부 연동 실패를 어떤 정책으로 흡수하는지 테스트와 함께 남긴다.
- 로그, retry, timeout, 보안 설정이 어느 레이어에 위치하는지 기록한다.

### 학습 포인트

- 운영 품질은 기능 구현 이후에 붙이는 장식이 아니라 설계 경계와 연결된다는 점을 확인한다.
- timeout, retry, fallback의 책임 위치를 Adapter/Application 관점에서 구분한다.
- 테스트 피라미드를 통해 어떤 테스트를 어디까지 작성할지 판단한다.

### 리뷰 질문

- 외부 시스템이 느리거나 실패해도 애플리케이션의 실패 범위가 제한되는가?
- 로그만 보고 주요 흐름과 실패 원인을 추적할 수 있는가?
- profile, secret, security 설정이 코드에 섞이지 않았는가?

### Done Criteria

- 장애 지점이 로그와 테스트로 추적 가능하다.
- 외부 시스템 실패가 전체 애플리케이션 실패로 바로 번지지 않는다.
- 운영 설정과 학습용 설정이 profile로 분리된다.

## 학습 순서 추천

1. 현재 API smoke test를 유지하면서 Service unit test를 추가한다.
2. 테스트가 충분해지면 레이어를 분리한다.
3. primitive 값을 Value Object로 바꾸고 도메인 규칙을 이동한다.
4. Strategy 선택 중복을 제거하면서 Factory/Registry를 실험한다.
5. payment 승인 흐름에 Chain of Responsibility 또는 Template Method를 적용한다.
6. notification 외부 연동을 Adapter/Decorator로 발전시킨다.
7. auth 정책 검증에 Specification과 Chain of Responsibility를 적용한다.
8. 이벤트 기반으로 도메인 간 결합도를 낮춘다.

## 변경 기록 템플릿

각 학습 PR 또는 커밋은 아래 형식으로 남긴다.

```markdown
## 목표

## As-Is

## To-Be

## 적용한 패턴 또는 설계 원칙

## 테스트

## 배운 점

## 남은 고민
```

실제 단계별 기록은 [LEARNING_LOG.md](LEARNING_LOG.md)에 누적한다.
