# Step 2. 레이어 분리

## Status

- [x] Not Started
- [ ] In Progress
- [ ] Done

## Goal

Controller 파일에 모여 있는 책임을 API, Application, Domain, Infrastructure 레이어로 분리한다. 패키지 이동 자체보다 의존성 방향을 명확히 하는 것을 목표로 한다.

## As-Is

- Controller 파일 하나에 여러 책임이 모여 있다.
- DTO와 도메인 모델이 구분되지 않는다.
- Service가 전략 선택과 유스케이스 실행을 동시에 담당한다.

## To-Be

- API 레이어는 HTTP 요청/응답과 validation만 담당한다.
- Application 레이어는 유스케이스와 트랜잭션 경계를 담당한다.
- Domain 레이어는 핵심 규칙과 모델을 담당한다.
- Infrastructure 레이어는 외부 시스템, DB, SDK 연동을 담당한다.

## Recommended Package Structure

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

## TODO

- [ ] `PaymentController`를 `payment.api`로 이동
- [ ] `PaymentService`를 `payment.application`으로 이동
- [ ] `PaymentProcessor`, `PaymentMethod`, `PaymentStatus`를 `payment.domain`으로 이동
- [ ] 카드/계좌이체 구현체를 `payment.infrastructure` 또는 `payment.domain.strategy`로 분리
- [ ] `notification`, `auth`도 같은 기준으로 분리
- [ ] API DTO와 도메인 Command/Result 객체 분리

## Change Log Points

- 이동 전 패키지 구조와 이동 후 패키지 구조를 비교한다.
- Controller, Application Service, Domain, Infrastructure가 각각 무엇을 모르게 되었는지 기록한다.
- 이동만 한 변경과 책임을 바꾼 변경을 구분해서 커밋한다.

## Learning Points

- 레이어 분리는 폴더 정리가 아니라 의존성 방향을 고정하는 작업임을 확인한다.
- HTTP DTO와 유스케이스 Command를 분리했을 때 테스트와 변경 범위가 어떻게 달라지는지 본다.
- Spring annotation이 도메인 모델로 새지 않게 만드는 기준을 세운다.

## Review Questions

- Controller가 비즈니스 규칙을 판단하고 있지 않은가?
- Application Service가 HTTP request/response 타입을 직접 알고 있지 않은가?
- Domain 코드는 Spring 없이 테스트할 수 있는가?

## Done Criteria

- Controller는 Application Service만 호출한다.
- Application Service는 HTTP 타입을 모른다.
- Domain 객체는 Spring MVC annotation을 모른다.
- 기존 API smoke test가 그대로 통과한다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
