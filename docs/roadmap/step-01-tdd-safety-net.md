# Step 1. TDD 안전망 만들기

## Status

- [x] Not Started
- [ ] In Progress
- [ ] Done

## Goal

구현을 바꾸기 전에 현재 동작과 실패 정책을 테스트로 고정한다. [Step 0.5](step-00-5-domain-baseline.md)에서 정의한 e-commerce 규칙 중 우선순위가 높은 주문 결제, 결제 알림, 고객 인증 시나리오를 테스트 후보로 삼아, 이후 레이어 분리와 도메인 모델링을 진행해도 동작이 깨졌는지 빠르게 확인할 수 있는 안전망을 만든다.

## As-Is

- API smoke test는 있지만 서비스 단위 테스트가 부족하다.
- 실패 케이스, 경계값, 지원하지 않는 타입에 대한 테스트가 부족하다.
- 도메인 규칙은 문서화되어 있지만 아직 테스트와 코드로 고정되어 있지 않다.
- `payment`, `notification`, `auth`가 아직 주문 흐름 안에서 연결되어 검증되지 않는다.
- 구현을 먼저 만들고 테스트로 확인하는 흐름이다.

## To-Be

- Service 단위 테스트를 먼저 작성한다.
- 지원 타입별 성공 케이스와 미지원 타입 실패 케이스를 테스트한다.
- validation 실패는 Controller/API 테스트에서 확인한다.
- 주문 결제, 결제 완료 알림, 고객 인증의 최소 happy path와 대표 실패 정책을 고정한다.
- 새 기능은 `Red -> Green -> Refactor` 순서로 진행한다.

## TODO

- [ ] `PaymentServiceTest` 추가: `CARD`, `BANK_TRANSFER` 성공 케이스 검증
- [ ] `NotificationServiceTest` 추가: `EMAIL`, `SMS`, `PUSH` 성공 케이스 검증
- [ ] `AuthServiceTest` 추가: `PASSWORD`, `OAUTH` 성공 케이스 검증
- [ ] 미지원 타입 처리 정책을 정하고 테스트로 고정
- [ ] 요청 필드 누락, 빈 문자열, 음수 금액 validation 테스트 추가
- [ ] Step 0.5의 e-commerce 테스트 후보 중 각 도메인별 실패 케이스 1개 이상 선택
- [ ] 주문 결제, 결제 완료 알림, 고객 인증 흐름의 최소 happy path 테스트 추가
- [ ] 테스트 이름을 요구사항 문장처럼 작성

## Change Log Points

- 테스트를 추가하기 전 현재 서비스의 책임과 실패 정책을 기록한다.
- 첫 실패 테스트가 어떤 요구사항을 표현하는지 남긴다.
- 테스트를 통과시키기 위해 최소 구현만 했는지 확인한다.

## Learning Points

- TDD의 `Red -> Green -> Refactor` 흐름을 실제 커밋 단위로 경험한다.
- 단위 테스트와 API 테스트의 책임 차이를 구분한다.
- 실패 케이스를 먼저 고정하면 리팩터링이 쉬워지는 이유를 확인한다.

## Review Questions

- 테스트가 구현 세부사항이 아니라 외부에서 기대하는 동작을 검증하는가?
- 새 전략 구현체를 추가할 때 기존 테스트가 의도한 안전망으로 동작하는가?
- 실패 케이스의 정책이 테스트 이름만 봐도 이해되는가?

## Done Criteria

- `./gradlew test`가 통과한다.
- 각 도메인마다 성공/실패 테스트가 최소 1개 이상 있다.
- 새 전략 구현체를 추가하기 전에 실패하는 테스트를 먼저 작성할 수 있다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
