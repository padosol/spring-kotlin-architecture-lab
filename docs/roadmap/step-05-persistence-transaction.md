# Step 5. 영속성과 트랜잭션 경계 추가

## Status

- [x] Not Started
- [ ] In Progress
- [ ] Done

## Goal

결제, 알림, 인증 흐름의 결과를 저장하고 트랜잭션 경계를 명확히 한다. JPA 편의성과 도메인 모델의 순수성 사이의 현실적인 균형을 비교한다.

## As-Is

- H2와 JPA 의존성은 있지만 Entity와 Repository가 없다.
- 결제/알림/인증 이력이 저장되지 않는다.
- 트랜잭션 경계가 없다.

## To-Be

- Application Service가 트랜잭션 경계를 가진다.
- Domain 모델과 JPA Entity를 직접 합칠지 분리할지 실험하고 비교한다.
- 저장소 구현은 port/adapter 구조로 분리한다.

## TODO

- [ ] `PaymentEntity`, `PaymentJpaRepository` 추가
- [ ] `PaymentRepository` port와 JPA adapter 구현
- [ ] 결제 요청/승인 결과 저장
- [ ] 알림 발송 이력 저장
- [ ] 로그인 성공/실패 이력 저장
- [ ] `@DataJpaTest` 또는 Boot 4 대응 JPA slice test 추가

## Change Log Points

- 트랜잭션 경계를 어디에 두었는지, 왜 그 위치가 적절한지 기록한다.
- Domain 모델과 JPA Entity를 합쳤는지 분리했는지, 그 이유를 남긴다.
- 실패 시 저장 정책과 롤백 정책을 테스트와 함께 기록한다.

## Learning Points

- Application Service가 유스케이스와 트랜잭션의 경계가 되는 이유를 이해한다.
- Repository port/adapter 구조가 테스트 대역과 인프라 교체를 어떻게 쉽게 만드는지 확인한다.
- JPA 편의성과 도메인 순수성 사이의 현실적인 균형을 경험한다.

## Review Questions

- Controller나 Domain이 JPA Repository를 직접 알지 않는가?
- 저장 실패, 외부 호출 실패, 부분 성공 상황의 정책이 명확한가?
- 테스트에서 실제 DB가 필요한 경우와 fake로 충분한 경우를 구분했는가?

## Done Criteria

- Application Service 테스트에서 Repository를 fake로 대체할 수 있다.
- JPA 세부사항이 Controller까지 새지 않는다.
- 실패 시 저장 정책이 테스트로 설명된다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
