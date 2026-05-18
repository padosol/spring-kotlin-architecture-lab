# Step 0.5. 도메인 베이스라인 정의

## Status

- [ ] Not Started
- [ ] In Progress
- [x] Done

## Goal

코드 구조는 일부러 단순하고 부족한 상태로 유지하되, `payment`, `notification`, `auth`가 다룰 업무 규칙과 실패 시나리오를 먼저 명확히 한다. 이후 TDD, 레이어 분리, DDD, 디자인 패턴 학습에서 비교할 수 있는 도메인 기준선을 만든다.

## As-Is

- 세 도메인은 성공 응답을 반환하는 샘플 API에 가깝다.
- `payment`는 결제 승인 성공만 있고 실패, 취소, 환불, 중복 결제 규칙이 없다.
- `notification`은 채널별 발송 성공만 있고 수신자 검증, 템플릿, 실패, 재시도 규칙이 없다.
- `auth`는 로그인 성공만 있고 계정 잠금, 비밀번호 정책, 토큰 만료, OAuth provider 차이가 없다.
- Controller, DTO, Service, Strategy 구현체가 한 파일에 모여 있어 아키텍처는 의도적으로 미성숙하다.

## To-Be

- 도메인 규칙은 문서로 먼저 정의하고, 코드에는 이후 단계에서 테스트와 함께 반영한다.
- 각 도메인은 정상 흐름, 실패 흐름, 상태 변화, 확장 지점을 가진다.
- Step 1에서는 이 문서의 규칙을 테스트 후보로 사용한다.
- Step 2 이후에는 같은 규칙을 유지한 채 책임 위치만 점진적으로 바꾼다.
- Step 3 이후에는 문자열과 숫자로 흩어진 규칙을 Value Object, Aggregate, Domain Service로 옮긴다.

## Domain Baseline

### Payment

주요 개념:

- `OrderId`: 결제 대상 주문 식별자. 빈 값이면 유효하지 않다.
- `Money`: 결제 금액. 0보다 커야 하며 통화 단위를 가진다.
- `PaymentMethod`: 결제 수단. 초기 후보는 `CARD`, `BANK_TRANSFER`다.
- `TransactionId`: 외부 결제 승인 또는 내부 거래 식별자다.
- `PaymentStatus`: `REQUESTED`, `APPROVED`, `FAILED`, `CANCELLED`, `REFUNDED`로 확장한다.

핵심 규칙:

- 금액이 0 이하이면 결제를 요청할 수 없다.
- 동일 주문에 대해 이미 승인된 결제가 있으면 중복 승인하지 않는다.
- 카드 결제는 승인 즉시 `APPROVED`가 될 수 있다.
- 계좌이체는 입금 대기 또는 승인 완료 상태를 가질 수 있다.
- 승인되지 않은 결제는 환불할 수 없다.
- 이미 환불된 결제는 다시 환불할 수 없다.
- 지원하지 않는 결제 수단은 도메인 실패로 다룬다.

확장 후보:

- 새 결제 수단 추가: `KAKAO_PAY`, `NAVER_PAY`, `POINT`
- 결제 전 검증 단계 추가: 주문 존재 확인, 한도 확인, 중복 요청 확인
- 결제 후 이벤트 추가: 결제 완료 알림, 결제 실패 알림
- 외부 PG adapter 추가

### Notification

주요 개념:

- `Recipient`: 수신자. 채널별로 형식 검증 기준이 다르다.
- `NotificationMessage`: 제목과 본문을 가진 메시지다.
- `NotificationChannel`: `EMAIL`, `SMS`, `PUSH`를 지원한다.
- `TemplateCode`: 재사용 가능한 메시지 템플릿 식별자다.
- `DeliveryStatus`: `REQUESTED`, `SENT`, `FAILED`, `RETRY_PENDING`으로 확장한다.

핵심 규칙:

- 이메일 수신자는 이메일 형식이어야 한다.
- SMS 수신자는 전화번호 형식이어야 한다.
- PUSH 수신자는 디바이스 토큰 또는 사용자 디바이스 식별자를 가져야 한다.
- 제목은 이메일과 PUSH에서는 필수지만, SMS에서는 선택일 수 있다.
- 본문은 비어 있을 수 없다.
- 채널별 메시지 길이 제한을 둘 수 있다.
- 발송 실패는 즉시 성공으로 숨기지 않고 실패 또는 재시도 대기 상태로 기록한다.
- 재시도 횟수는 제한한다.

확장 후보:

- 외부 발송 SDK adapter 추가
- 발송 실패 retry decorator 추가
- 결제 완료, 로그인 성공 같은 도메인 이벤트를 구독해 알림 발송
- 템플릿 렌더링과 변수 검증 추가

### Auth

주요 개념:

- `AuthPrincipal`: 로그인 주체. 이메일, 사용자 ID, OAuth subject로 확장 가능하다.
- `Credential`: 비밀번호, OAuth authorization code, access token 같은 인증 재료다.
- `AuthType`: `PASSWORD`, `OAUTH`를 지원한다.
- `AuthToken`: 인증 성공 후 발급되는 토큰이다.
- `AccountStatus`: `ACTIVE`, `LOCKED`, `DISABLED`로 확장한다.

핵심 규칙:

- principal과 credential은 비어 있을 수 없다.
- 비밀번호 인증은 저장된 사용자와 credential 검증을 통과해야 한다.
- 비밀번호 실패 횟수가 기준을 넘으면 계정을 잠근다.
- 잠긴 계정은 인증할 수 없다.
- OAuth 인증은 provider별 검증 방식이 다를 수 있다.
- 인증 토큰은 만료 시간을 가진다.
- 지원하지 않는 인증 방식은 도메인 실패로 다룬다.

확장 후보:

- 비밀번호 정책 Specification 추가
- 계정 잠금 정책 Specification 추가
- OAuth provider adapter 추가
- 토큰 발급 strategy 추가
- API key, session, refresh token 인증 방식 추가

## Intentionally Weak Architecture

이 단계에서는 아래 약점을 일부러 남긴다.

- Controller, DTO, Service, Strategy 구현체가 한 파일에 함께 있다.
- HTTP 예외와 애플리케이션 실패 정책이 분리되어 있지 않다.
- 도메인 모델과 API DTO가 분리되어 있지 않다.
- Repository port와 Infrastructure adapter가 없다.
- 외부 시스템 연동은 실제 adapter 없이 문자열 ID로만 표현한다.
- 트랜잭션 경계와 영속성 모델이 없다.

이 약점들은 Step 1 이후의 학습 재료로 사용한다.

## Test Seeds

Step 1에서 우선순위가 높은 테스트 후보:

- Payment: 정상 카드 결제는 승인된다.
- Payment: 0 이하 금액은 거절된다.
- Payment: 이미 승인된 주문은 중복 승인되지 않는다.
- Notification: 이메일 채널은 이메일 형식 수신자만 허용한다.
- Notification: 본문이 비어 있으면 발송하지 않는다.
- Notification: 발송 실패는 실패 상태 또는 재시도 대기로 남는다.
- Auth: 올바른 비밀번호는 토큰을 발급한다.
- Auth: 잘못된 비밀번호는 인증 실패로 처리한다.
- Auth: 잠긴 계정은 인증할 수 없다.

## Change Log Points

- 도메인 규칙을 코드보다 먼저 정리한 이유를 기록한다.
- 어떤 규칙을 Step 1 테스트로 먼저 고정할지 선택한다.
- 지금 당장 코드에 넣지 않고 나중 단계로 미룬 규칙을 기록한다.

## Learning Points

- 좋은 학습용 As-Is는 아무 규칙이 없는 코드가 아니라, 규칙은 존재하지만 구조가 미성숙한 코드다.
- 도메인 규칙을 먼저 정리하면 TDD의 첫 실패 테스트를 요구사항 문장으로 작성하기 쉽다.
- 아키텍처를 처음부터 완성하지 않아야 레이어 분리와 DDD 적용 전후를 비교할 수 있다.

## Review Questions

- 각 도메인에 정상 흐름, 실패 흐름, 상태 변화가 모두 있는가?
- Step 1에서 바로 테스트로 옮길 수 있는 규칙이 충분히 구체적인가?
- 아직 코드에 반영하지 않을 규칙과 바로 반영할 규칙이 구분되어 있는가?
- 현재 아키텍처의 부족함이 이후 단계의 학습 포인트로 남아 있는가?

## Done Criteria

- 세 도메인의 주요 개념, 핵심 규칙, 확장 후보가 문서화되어 있다.
- Step 1에서 작성할 테스트 후보가 도출되어 있다.
- 코드 구조는 아직 의도적으로 미성숙한 상태로 남아 있다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
