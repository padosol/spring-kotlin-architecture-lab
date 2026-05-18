# Step 3. DDD 기반 도메인 모델링

## Status

- [x] Not Started
- [ ] In Progress
- [ ] Done

## Goal

단순 문자열과 숫자로 표현된 핵심 개념을 Value Object와 Aggregate로 옮긴다. [Step 0.5](step-00-5-domain-baseline.md)에서 정의한 도메인 규칙이 Controller, DTO, JPA 세부사항에 흩어지지 않도록 만든다.

## As-Is

- `orderId`, `principal`, `recipient`가 단순 문자열이다.
- 금액, 인증 정보, 알림 대상 같은 개념의 불변식이 흩어질 가능성이 크다.
- 아직 Aggregate, Value Object, Domain Service의 기준이 없다.

## To-Be

- 중요한 값은 Value Object로 감싼다.
- 도메인 규칙은 생성자나 팩토리에서 보장한다.
- 유스케이스별 Command와 Result를 명시한다.
- Repository는 인터페이스를 Domain/Application 쪽에 두고 구현은 Infrastructure에 둔다.

## TODO

- [ ] `Money`, `OrderId`, `TransactionId` Value Object 추가
- [ ] `Payment` Aggregate 추가: 결제 요청, 승인, 실패 상태 관리
- [ ] `NotificationMessage`, `Recipient` Value Object 추가
- [ ] `AuthPrincipal`, `Credential` Value Object 추가
- [ ] 도메인 객체 생성 실패 케이스 테스트 추가
- [ ] Repository port 정의: `PaymentRepository`, `NotificationHistoryRepository`, `UserCredentialRepository`

## Change Log Points

- primitive 값이 Value Object로 바뀌면서 어느 규칙이 한곳으로 모였는지 기록한다.
- Aggregate를 도입한 이유와 Aggregate에 넣지 않은 책임을 함께 적는다.
- Repository port의 위치와 Infrastructure 구현체의 의존성 방향을 기록한다.

## Learning Points

- Value Object가 validation 중복과 잘못된 상태 생성을 어떻게 줄이는지 확인한다.
- Aggregate가 상태 변경의 진입점을 제한하는 방식을 경험한다.
- DDD를 JPA Entity 설계와 동일시하지 않고 도메인 규칙 중심으로 바라본다.

## Review Questions

- 잘못된 금액, 빈 식별자, 잘못된 인증 정보가 객체 생성 시점에 막히는가?
- 도메인 객체가 DB 저장 방식이나 HTTP 표현 방식에 묶여 있지 않은가?
- Aggregate가 너무 많은 책임을 가지거나 반대로 의미 없는 wrapper가 되지는 않았는가?

## Done Criteria

- 잘못된 도메인 상태를 만들기 어렵다.
- primitive obsession이 줄어든다.
- 도메인 테스트는 Spring context 없이 빠르게 실행된다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
