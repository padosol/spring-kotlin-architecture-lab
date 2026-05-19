# Step 0.5. E-commerce 도메인 베이스라인 정의

## Status

- [ ] Not Started
- [ ] In Progress
- [x] Done

## Goal

코드 구조는 일부러 단순하고 부족한 상태로 유지하되, 이 프로젝트가 e-commerce 도메인을 학습하기 위한 프로젝트임을 명확히 한다. `payment`, `notification`, `auth`는 독립 기능이 아니라 쇼핑몰의 주문, 결제, 고객 커뮤니케이션, 고객 인증 흐름 안에서 다뤄야 한다.

이 단계의 목표는 완성된 아키텍처를 만드는 것이 아니라, 이후 TDD, 레이어 분리, DDD, 디자인 패턴 학습에서 비교할 수 있는 도메인 기준선을 만드는 것이다.

## Step Output

Step 0.5의 산출물은 코드 구현이 아니라 학습 기준선이다.

- 코드 변경은 하지 않는다.
- e-commerce 도메인 용어, 규칙, 확장 후보를 문서로 고정한다.
- 현재 코드의 부족함을 의도적으로 남겨 Step 1 이후의 테스트, 리팩터링, 패턴 적용 재료로 사용한다.
- 다음 단계에서 어떤 규칙을 먼저 테스트로 옮길지 우선순위를 정한다.

## Code Baseline Snapshot

현재 코드는 아래 상태를 Step 0.5의 As-Is로 고정한다.

### Payment Code

- `PaymentController.kt` 한 파일에 API DTO, Controller, Service, Strategy interface, Strategy 구현체가 모두 있다.
- `CARD`, `BANK_TRANSFER`는 모두 즉시 `APPROVED`를 반환한다.
- `orderId`와 `amount`는 API validation만 있고 주문 소유자, 주문 상태, 주문 금액 일치 여부는 확인하지 않는다.
- `PaymentStatus`는 `APPROVED` 하나뿐이라 실패, 요청, 취소, 환불 상태를 표현할 수 없다.
- 미지원 결제 수단은 `ResponseStatusException`으로 직접 HTTP 오류가 된다.

### Notification Code

- `NotificationController.kt` 한 파일에 API DTO, Controller, Service, Strategy interface, Strategy 구현체가 모두 있다.
- `EMAIL`, `SMS`, `PUSH`는 모두 즉시 `SENT`를 반환한다.
- 이메일 형식, 전화번호 형식, 디바이스 토큰 형식 검증이 없다.
- 주문, 결제, 인증 이벤트와 연결되어 있지 않고 사용자가 직접 제목과 본문을 넘긴다.
- 발송 실패, 재시도, 템플릿, 고객 수신 선호도 정책이 없다.

### Auth Code

- `AuthController.kt` 한 파일에 API DTO, Controller, Service, Strategy interface, Strategy 구현체가 모두 있다.
- `PASSWORD`, `OAUTH`는 credential 검증 없이 항상 인증 성공을 반환한다.
- 계정 저장소, 고객 식별자, 역할, 계정 잠금, 비활성 계정, 토큰 만료가 없다.
- 인증 결과가 주문이나 결제 API 권한 검증과 연결되어 있지 않다.
- Spring Security 설정은 모든 요청을 허용한다.

## Why No Code Change In Step 0.5

이 단계에서 `Order`, `Customer`, `PaymentAttempt`, `NotificationEvent`를 바로 구현하지 않는 이유:

- Step 1에서 TDD로 현재 동작과 실패 정책을 먼저 고정하기 위해서다.
- Step 2에서 레이어 분리 전후를 비교할 수 있어야 한다.
- Step 3에서 DDD 모델링을 적용할 때 문자열과 숫자로 된 현재 구조와 Value Object, Aggregate를 비교할 수 있어야 한다.
- Step 4에서 디자인 패턴을 목적 없이 먼저 넣지 않고, 실제 변경 압력이 생긴 뒤 적용하기 위해서다.

Step 0.5는 "도메인은 명확하지만 구현과 구조는 아직 부족한 상태"를 만드는 단계다.

## As-Is

- 세 도메인은 성공 응답을 반환하는 샘플 API에 가깝다.
- `payment`는 주문 결제처럼 보이지만 주문 상태, 결제 시도, 중복 결제, 취소, 환불 규칙이 없다.
- `notification`은 채널별 발송 성공만 있고 주문 완료, 결제 실패, 배송 시작 같은 e-commerce 이벤트와 연결되어 있지 않다.
- `auth`는 로그인 성공만 있고 고객 계정, 판매자 계정, 관리자 권한, 계정 잠금, 토큰 만료 규칙이 없다.
- `order`, `cart`, `catalog`, `inventory`, `shipment`는 아직 코드에 없고 문서상 주변 도메인으로만 다룬다.
- Controller, DTO, Service, Strategy 구현체가 한 파일에 모여 있어 아키텍처는 의도적으로 미성숙하다.

## To-Be

- 도메인 규칙은 문서로 먼저 정의하고, 코드에는 이후 단계에서 테스트와 함께 반영한다.
- `payment`, `notification`, `auth`는 e-commerce 주문 흐름의 일부로 설명한다.
- 각 도메인은 정상 흐름, 실패 흐름, 상태 변화, 확장 지점을 가진다.
- Step 1에서는 이 문서의 규칙을 테스트 후보로 사용한다.
- Step 2 이후에는 같은 규칙을 유지한 채 책임 위치만 점진적으로 바꾼다.
- Step 3 이후에는 문자열과 숫자로 흩어진 규칙을 Value Object, Aggregate, Domain Service로 옮긴다.

## E-commerce Scope

### 학습 범위

이 프로젝트에서 직접 구현하며 학습할 초기 범위:

- `auth`: 고객이 쇼핑몰에 로그인하고 API를 사용할 수 있게 하는 인증 흐름
- `payment`: 주문에 대한 결제 승인, 실패, 취소, 환불 흐름
- `notification`: 주문, 결제, 인증 이벤트를 고객에게 전달하는 알림 흐름

### 주변 도메인

초기 코드에는 만들지 않지만, 설계 판단에 영향을 주는 주변 도메인:

- `catalog`: 상품, 가격, 판매 상태
- `cart`: 장바구니, 선택 옵션, 수량
- `order`: 주문 생성, 주문 상태, 주문 금액, 주문자 정보
- `inventory`: 재고 예약, 재고 차감, 품절
- `shipment`: 배송 요청, 운송장, 배송 상태
- `promotion`: 쿠폰, 포인트, 할인 정책

주변 도메인은 Step 0.5에서 모두 구현하지 않는다. 대신 `payment`, `notification`, `auth`가 어떤 정보를 외부 경계에서 받아야 하는지 판단하는 기준으로 사용한다.

### 대표 사용자 흐름

1. 고객이 `auth`를 통해 로그인한다.
2. 고객이 장바구니 상품으로 주문을 생성한다.
3. 주문 금액과 주문 상태를 기준으로 `payment`가 결제를 요청한다.
4. 결제 결과에 따라 주문은 결제 완료, 결제 실패, 결제 대기 상태로 이동할 수 있다.
5. `notification`은 결제 완료, 결제 실패, 로그인 보안 알림 같은 이벤트를 고객에게 발송한다.
6. 이후 단계에서 배송, 환불, 주문 취소 이벤트를 추가할 수 있다.

## Ubiquitous Language

이 문서와 테스트 이름에서 사용할 공통 용어:

- `Customer`: 쇼핑몰에서 상품을 구매하는 사용자
- `Account`: 인증 가능한 고객, 판매자, 관리자 계정
- `Order`: 고객이 구매하려는 상품, 금액, 배송 정보를 묶은 거래 단위
- `OrderId`: 주문 식별자
- `OrderStatus`: 주문의 현재 상태. 후보는 `CREATED`, `PAYMENT_PENDING`, `PAID`, `PAYMENT_FAILED`, `CANCELLED`, `REFUNDED`, `SHIPPING`, `DELIVERED`
- `Payment`: 주문에 대한 결제 행위와 결과
- `PaymentAttempt`: 같은 주문에 대해 여러 번 시도될 수 있는 결제 요청
- `Notification`: 고객에게 전달되는 주문, 결제, 인증 관련 메시지
- `DomainEvent`: 결제 승인, 결제 실패, 로그인 성공, 계정 잠금 같은 도메인에서 발생한 사건

## Domain Baseline

### Payment

e-commerce 안에서의 책임:

- 주문의 결제 가능 여부를 확인하고 결제 시도를 만든다.
- 외부 PG 또는 내부 결제 수단을 통해 결제를 승인한다.
- 결제 결과를 주문 흐름이 사용할 수 있는 상태와 이벤트로 남긴다.

주요 개념:

- `OrderId`: 결제 대상 주문 식별자. 빈 값이면 유효하지 않다.
- `CustomerId`: 결제를 요청한 고객 식별자. 주문의 소유자와 일치해야 한다.
- `Money`: 결제 금액. 0보다 커야 하며 통화 단위를 가진다.
- `PaymentMethod`: 결제 수단. 초기 후보는 `CARD`, `BANK_TRANSFER`다.
- `PaymentAttemptId`: 결제 재시도와 중복 요청을 구분하는 식별자다.
- `TransactionId`: 외부 결제 승인 또는 내부 거래 식별자다.
- `PaymentStatus`: `REQUESTED`, `APPROVED`, `FAILED`, `CANCELLED`, `REFUNDED`로 확장한다.

핵심 규칙:

- 주문이 존재하지 않으면 결제를 요청할 수 없다.
- 주문자가 아닌 고객은 해당 주문을 결제할 수 없다.
- 주문 금액과 결제 금액이 다르면 결제를 승인하지 않는다.
- 금액이 0 이하이면 결제를 요청할 수 없다.
- 이미 결제 완료된 주문은 중복 승인하지 않는다.
- 같은 `PaymentAttemptId`로 들어온 요청은 멱등하게 처리한다.
- 카드 결제는 승인 즉시 `APPROVED`가 될 수 있다.
- 계좌이체는 입금 대기 또는 승인 완료 상태를 가질 수 있다.
- 주문이 취소된 뒤에는 결제를 승인할 수 없다.
- 승인되지 않은 결제는 환불할 수 없다.
- 이미 환불된 결제는 다시 환불할 수 없다.
- 지원하지 않는 결제 수단은 도메인 실패로 다룬다.

확장 후보:

- 새 결제 수단 추가: `KAKAO_PAY`, `NAVER_PAY`, `POINT`, `COUPON`, `GIFT_CARD`
- 결제 전 검증 단계 추가: 주문 존재 확인, 주문자 확인, 한도 확인, 중복 요청 확인
- 결제 후 이벤트 추가: `PaymentApproved`, `PaymentFailed`, `PaymentRefunded`
- 외부 PG adapter 추가
- 주문 취소와 환불 정책 분리
- 포인트, 쿠폰, 복합 결제 추가

### Notification

e-commerce 안에서의 책임:

- 주문, 결제, 인증 이벤트를 고객이 이해할 수 있는 메시지로 변환한다.
- 채널별 제약을 지키면서 외부 발송 시스템으로 전달한다.
- 실패한 발송을 기록하고 재시도 또는 보상 처리를 할 수 있게 한다.

주요 개념:

- `Recipient`: 수신자. 채널별로 형식 검증 기준이 다르다.
- `NotificationMessage`: 제목과 본문을 가진 메시지다.
- `NotificationChannel`: `EMAIL`, `SMS`, `PUSH`를 지원한다.
- `TemplateCode`: 재사용 가능한 메시지 템플릿 식별자다.
- `NotificationEventType`: `ORDER_CREATED`, `PAYMENT_APPROVED`, `PAYMENT_FAILED`, `ORDER_CANCELLED`, `LOGIN_ALERT` 같은 발송 원인이다.
- `DeliveryStatus`: `REQUESTED`, `SENT`, `FAILED`, `RETRY_PENDING`으로 확장한다.

핵심 규칙:

- 이메일 수신자는 이메일 형식이어야 한다.
- SMS 수신자는 전화번호 형식이어야 한다.
- PUSH 수신자는 디바이스 토큰 또는 사용자 디바이스 식별자를 가져야 한다.
- 제목은 이메일과 PUSH에서는 필수지만, SMS에서는 선택일 수 있다.
- 본문은 비어 있을 수 없다.
- 주문, 결제 알림은 주문번호 또는 결제 식별자를 포함해야 한다.
- 채널별 메시지 길이 제한을 둔다.
- 고객이 수신 거부한 마케팅 알림은 발송하지 않는다.
- 결제, 주문 상태처럼 거래성 알림은 마케팅 수신 동의와 분리해서 다룬다.
- 발송 실패는 즉시 성공으로 숨기지 않고 실패 또는 재시도 대기 상태로 기록한다.
- 재시도 횟수는 제한한다.

확장 후보:

- 외부 발송 SDK adapter 추가
- 발송 실패 retry decorator 추가
- 결제 완료, 결제 실패, 주문 취소 같은 도메인 이벤트를 구독해 알림 발송
- 템플릿 렌더링과 변수 검증 추가
- 고객 알림 선호도 정책 추가
- Transactional notification과 marketing notification 분리

### Auth

e-commerce 안에서의 책임:

- 고객, 판매자, 관리자가 본인 계정으로 인증할 수 있게 한다.
- 인증 결과를 주문, 결제 API가 사용할 수 있는 사용자 식별 정보로 제공한다.
- 계정 상태와 위험 로그인 흐름을 통해 거래를 보호한다.

주요 개념:

- `AuthPrincipal`: 로그인 주체. 이메일, 사용자 ID, OAuth subject로 확장 가능하다.
- `Credential`: 비밀번호, OAuth authorization code, access token 같은 인증 재료다.
- `AuthType`: `PASSWORD`, `OAUTH`를 지원한다.
- `AuthToken`: 인증 성공 후 발급되는 토큰이다.
- `AccountId`: 쇼핑몰 계정 식별자다.
- `CustomerId`: 주문과 결제에서 사용하는 고객 식별자다.
- `AccountRole`: `CUSTOMER`, `SELLER`, `ADMIN`으로 확장한다.
- `AccountStatus`: `ACTIVE`, `LOCKED`, `DISABLED`로 확장한다.

핵심 규칙:

- principal과 credential은 비어 있을 수 없다.
- 비밀번호 인증은 저장된 사용자와 credential 검증을 통과해야 한다.
- 비밀번호 실패 횟수가 기준을 넘으면 계정을 잠근다.
- 잠긴 계정은 인증할 수 없다.
- 비활성 계정은 주문과 결제를 수행할 수 없다.
- OAuth 인증은 provider별 검증 방식이 다를 수 있다.
- 인증 토큰은 만료 시간을 가진다.
- 결제, 주문 변경 API는 인증된 고객 식별자를 요구한다.
- 판매자와 관리자는 고객 주문 결제 권한과 다른 권한 체계를 가진다.
- 지원하지 않는 인증 방식은 도메인 실패로 다룬다.

확장 후보:

- 비밀번호 정책 Specification 추가
- 계정 잠금 정책 Specification 추가
- OAuth provider adapter 추가
- 토큰 발급 strategy 추가
- API key, session, refresh token 인증 방식 추가
- Role 또는 Permission 기반 인가 정책 추가
- 위험 로그인 탐지와 보안 알림 이벤트 추가

## Bounded Context Candidates

초기에는 코드 패키지를 `payment`, `notification`, `auth`로 유지한다. DDD 단계에서 아래 경계를 검토한다.

- `Identity and Access`: 계정, 인증, 토큰, 권한
- `Ordering`: 주문, 주문 상태, 주문 금액, 주문 취소
- `Payment`: 결제 시도, 승인, 실패, 환불
- `Notification`: 메시지 템플릿, 발송, 재시도, 고객 선호도
- `Catalog`: 상품, 가격, 판매 가능 상태
- `Inventory`: 재고 예약, 재고 차감, 재고 복구
- `Shipping`: 배송 요청, 배송 상태, 운송장

Step 0.5에서는 `Ordering`, `Catalog`, `Inventory`, `Shipping`을 직접 구현하지 않는다. 대신 결제와 알림의 입력, 이벤트, 실패 정책을 이해하기 위한 배경으로 둔다.

## Intentionally Weak Architecture

이 단계에서는 아래 약점을 일부러 남긴다.

- Controller, DTO, Service, Strategy 구현체가 한 파일에 함께 있다.
- HTTP 예외와 애플리케이션 실패 정책이 분리되어 있지 않다.
- 도메인 모델과 API DTO가 분리되어 있지 않다.
- `Order`, `Customer`, `Payment`, `Notification` 같은 모델이 풍부한 도메인 객체가 아니라 문자열과 숫자로만 표현된다.
- 주문, 결제, 알림 사이의 이벤트 흐름이 없다.
- Repository port와 Infrastructure adapter가 없다.
- 외부 시스템 연동은 실제 adapter 없이 문자열 ID로만 표현한다.
- 트랜잭션 경계와 영속성 모델이 없다.
- 인증 결과가 결제, 주문 요청의 권한 검증과 연결되어 있지 않다.

이 약점들은 Step 1 이후의 학습 재료로 사용한다.

## Test Seeds

Step 1에서 우선순위가 높은 테스트 후보:

- Payment: 정상 카드 결제는 주문 `order-1`에 대해 승인된다.
- Payment: 0 이하 금액은 거절된다.
- Payment: 이미 결제 완료된 주문은 중복 승인되지 않는다.
- Payment: 결제 금액이 주문 금액과 다르면 승인되지 않는다.
- Payment: 취소된 주문은 결제할 수 없다.
- Notification: 결제 완료 이벤트는 고객에게 결제 완료 알림을 만든다.
- Notification: 이메일 채널은 이메일 형식 수신자만 허용한다.
- Notification: 본문이 비어 있으면 발송하지 않는다.
- Notification: 발송 실패는 실패 상태 또는 재시도 대기로 남는다.
- Auth: 올바른 고객 비밀번호는 고객 토큰을 발급한다.
- Auth: 잘못된 비밀번호는 인증 실패로 처리한다.
- Auth: 잠긴 고객 계정은 인증할 수 없다.
- Auth: 인증되지 않은 고객은 결제를 요청할 수 없다.

## Step 1 Priority Cut

Step 1에서는 모든 규칙을 한 번에 구현하지 않는다. 테스트 안전망을 만드는 것이 목표이므로 아래 순서로 진행한다.

1. 현재 API happy path를 유지한다.
2. Service 단위에서 Strategy 선택 성공과 실패를 테스트한다.
3. API validation 실패를 테스트한다.
4. e-commerce 규칙은 각 도메인에서 하나씩만 대표 실패 케이스를 먼저 고른다.
5. 더 큰 상태 변화가 필요한 규칙은 Step 3 이후 도메인 모델링 단계로 넘긴다.

우선순위가 높은 대표 실패 후보:

- Payment: 주문 금액과 결제 금액 불일치
- Notification: 이메일 채널의 잘못된 수신자 형식
- Auth: 잘못된 비밀번호 인증 실패

## Change Log Points

- e-commerce 전체 흐름 중 이번 프로젝트가 직접 구현할 범위와 미룰 범위를 기록한다.
- 도메인 규칙을 코드보다 먼저 정리한 이유를 기록한다.
- 어떤 규칙을 Step 1 테스트로 먼저 고정할지 선택한다.
- 지금 당장 코드에 넣지 않고 나중 단계로 미룬 규칙을 기록한다.
- 주변 도메인을 아직 구현하지 않는 이유와 그로 인한 테스트 한계를 기록한다.

## Learning Points

- 좋은 학습용 As-Is는 아무 규칙이 없는 코드가 아니라, 규칙은 존재하지만 구조가 미성숙한 코드다.
- e-commerce는 결제, 알림, 인증이 따로 존재하는 기능이 아니라 주문 흐름 안에서 의미가 생긴다.
- 도메인 규칙을 먼저 정리하면 TDD의 첫 실패 테스트를 요구사항 문장으로 작성하기 쉽다.
- 아키텍처를 처음부터 완성하지 않아야 레이어 분리와 DDD 적용 전후를 비교할 수 있다.
- 주변 도메인을 모두 구현하지 않아도, 경계와 용어를 먼저 정의하면 설계 방향을 잃지 않는다.

## Review Questions

- `payment`, `notification`, `auth`가 e-commerce 주문 흐름 안에서 설명되는가?
- 각 도메인에 정상 흐름, 실패 흐름, 상태 변화가 모두 있는가?
- Step 1에서 바로 테스트로 옮길 수 있는 규칙이 충분히 구체적인가?
- 주변 도메인 중 지금 구현하지 않을 범위가 명확한가?
- 아직 코드에 반영하지 않을 규칙과 바로 반영할 규칙이 구분되어 있는가?
- 현재 아키텍처의 부족함이 이후 단계의 학습 포인트로 남아 있는가?

## Done Criteria

- e-commerce 학습 범위와 주변 도메인이 문서화되어 있다.
- 세 도메인의 주요 개념, 핵심 규칙, 확장 후보가 e-commerce 맥락으로 문서화되어 있다.
- 현재 코드의 부족함이 코드 베이스라인 스냅샷으로 기록되어 있다.
- Step 0.5에서 코드 변경을 하지 않는 이유가 기록되어 있다.
- Step 1에서 작성할 테스트 후보가 도출되어 있다.
- Step 1에서 먼저 다룰 테스트 범위가 우선순위로 잘려 있다.
- 코드 구조는 아직 의도적으로 미성숙한 상태로 남아 있다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
