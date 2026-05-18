# Step 8. 실전 품질 개선

## Status

- [x] Not Started
- [ ] In Progress
- [ ] Done

## Goal

학습용 구조를 운영 관점의 품질로 확장한다. timeout, retry, fallback, 보안, 로그, CI 같은 비기능 요구사항이 어느 레이어에 위치해야 하는지 확인한다.

## As-Is

- 로깅, 관측성, 보안 정책, 운영 설정이 최소 수준이다.
- 실제 외부 연동 실패, 재시도, timeout 정책이 없다.
- 성능이나 동시성 요구사항이 정의되어 있지 않다.

## To-Be

- 외부 연동에는 timeout, retry, fallback 정책을 둔다.
- 보안은 학습용 permit-all에서 실제 인증/인가 흐름으로 발전시킨다.
- 테스트 피라미드를 갖춘다: unit, slice, integration, API contract.

## TODO

- [ ] structured logging 적용
- [ ] 외부 adapter timeout/retry 정책 추가
- [ ] Spring Security 인증 흐름을 실제 auth 도메인과 연결
- [ ] Testcontainers 도입 검토
- [ ] CI에서 `./gradlew test` 실행
- [ ] 성능/동시성 시나리오 정의

## Change Log Points

- 운영 설정과 학습용 설정을 어떻게 분리했는지 기록한다.
- 외부 연동 실패를 어떤 정책으로 흡수하는지 테스트와 함께 남긴다.
- 로그, retry, timeout, 보안 설정이 어느 레이어에 위치하는지 기록한다.

## Learning Points

- 운영 품질은 기능 구현 이후에 붙이는 장식이 아니라 설계 경계와 연결된다는 점을 확인한다.
- timeout, retry, fallback의 책임 위치를 Adapter/Application 관점에서 구분한다.
- 테스트 피라미드를 통해 어떤 테스트를 어디까지 작성할지 판단한다.

## Review Questions

- 외부 시스템이 느리거나 실패해도 애플리케이션의 실패 범위가 제한되는가?
- 로그만 보고 주요 흐름과 실패 원인을 추적할 수 있는가?
- profile, secret, security 설정이 코드에 섞이지 않았는가?

## Done Criteria

- 장애 지점이 로그와 테스트로 추적 가능하다.
- 외부 시스템 실패가 전체 애플리케이션 실패로 바로 번지지 않는다.
- 운영 설정과 학습용 설정이 profile로 분리된다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
