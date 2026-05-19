# Roadmap Details

이 디렉터리는 e-commerce 아키텍처 학습을 위한 단계별 상세 로드맵을 보관한다. `TODO.md`는 진행 상황을 빠르게 확인하는 체크리스트로 유지하고, 각 단계의 `As-Is`, `To-Be`, 변경 기록 포인트, 학습 포인트, 리뷰 질문은 이 디렉터리의 Step 문서에 둔다.

핵심 학습 범위는 `payment`, `notification`, `auth`다. 다만 e-commerce 구색을 위해 Step 0.5에서 `customer`, `catalog`, `cart`, `order`, `inventory`, `shipment`, `promotion`을 얇은 API 기준선으로 추가한다. 이 구색 도메인들은 완성된 설계가 아니라 이후 TDD, 레이어 분리, DDD에서 책임과 경계를 개선할 학습 재료다.

## 문서 구조

- [Step 0.5. E-commerce 도메인 베이스라인 정의](step-00-5-domain-baseline.md)
- [Step 1. TDD 안전망 만들기](step-01-tdd-safety-net.md)
- [Step 2. 레이어 분리](step-02-layer-separation.md)
- [Step 3. DDD 기반 도메인 모델링](step-03-ddd-domain-modeling.md)
- [Step 4. 디자인 패턴을 목적별로 적용](step-04-design-patterns.md)
- [Step 5. 영속성과 트랜잭션 경계 추가](step-05-persistence-transaction.md)
- [Step 6. 에러 처리와 API 계약 정리](step-06-error-api-contract.md)
- [Step 7. 확장성, 유지보수성, 재사용성 강화](step-07-extensibility-maintainability-reuse.md)
- [Step 8. 실전 품질 개선](step-08-operational-quality.md)
- [Step 기록 템플릿](step-template.md)

## 학습 기록 방식

각 단계는 코드 변경만 남기지 않고, 변경 전후의 설계 판단을 함께 기록한다. 목표는 나중에 커밋 이력과 문서를 함께 보면서 "왜 이 구조로 바꿨는지", "어떤 문제가 줄었는지", "다음에는 무엇을 개선해야 하는지"를 복기할 수 있게 만드는 것이다.

## 단계 시작 전 기록

- `As-Is`: 현재 코드의 책임 배치, 의존성 방향, 테스트 상태, 변경 비용을 적는다.
- `Problem`: 지금 구조에서 학습하거나 개선하려는 불편함을 한두 문장으로 적는다.
- `To-Be`: 이번 단계가 끝났을 때 코드가 어떤 형태가 되어야 하는지 적는다.
- `검증 방법`: 어떤 테스트, 실행 명령, 코드 리뷰 기준으로 완료 여부를 확인할지 적는다.

## 단계 진행 중 기록

- 실패한 테스트 또는 깨진 설계를 먼저 기록한다.
- 선택한 설계 대안과 선택하지 않은 대안을 함께 적는다.
- 패턴을 적용할 때는 패턴 이름보다 변경 지점, 의존성 방향, 테스트 용이성을 먼저 적는다.
- 커밋은 학습 단위가 보이도록 작게 나눈다.

## 단계 완료 후 리뷰

- `What changed`: 실제로 바뀐 파일, 패키지, 의존성, 테스트를 요약한다.
- `What improved`: 확장성, 유지보수성, 재사용성, 테스트 용이성 중 좋아진 부분을 적는다.
- `Trade-off`: 코드가 늘어난 부분, 복잡해진 부분, 아직 과한 추상화로 보이는 부분을 적는다.
- `Learning point`: 이번 단계에서 배운 설계 원칙, 패턴 적용 기준, Spring/Kotlin 사용법을 적는다.
- `Next action`: 다음 단계에서 이어서 검증하거나 정리할 항목을 적는다.

실제 단계별 회고는 [Learning Log](../LEARNING_LOG.md)에 누적한다.
