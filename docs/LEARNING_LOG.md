# Learning Log

이 문서는 e-commerce 도메인의 주문 흐름 안에서 `payment`, `notification`, `auth`를 단계적으로 개선하면서 배운 점을 누적하는 기록장이다. 각 단계가 끝날 때 커밋 해시와 함께 `As-Is`, `To-Be`, 변경 내용, 테스트, 학습 포인트를 남긴다.

## 작성 원칙

- 한 단계가 끝날 때마다 하나의 섹션을 추가한다.
- 커밋 해시는 나중에 코드를 바로 찾아볼 수 있도록 반드시 적는다.
- `As-Is`와 `To-Be`는 코드 구조 관점으로 적고, 감상문처럼 쓰지 않는다.
- `Learning Points`에는 다음에 비슷한 설계를 할 때 재사용할 판단 기준을 적는다.
- `Trade-Offs`에는 이번 선택으로 생긴 비용도 함께 적는다.

## Step Review Template

```markdown
## Step N. 제목

### Date

- YYYY-MM-DD

### Commit

- `commit-hash` commit message

### As-Is

- 변경 전 책임 배치:
- 변경 전 의존성 방향:
- 변경 전 테스트 상태:
- 변경 전 변경 비용:

### Problem

- 이번 단계에서 해결하려는 문제:
- 그냥 두면 커질 위험:

### To-Be

- 변경 후 책임 배치:
- 변경 후 의존성 방향:
- 변경 후 테스트 상태:
- 기대하는 변경 비용:

### Changes

- 코드 변경:
- 패키지/레이어 변경:
- 설계 패턴 또는 원칙:

### Tests

- 추가한 테스트:
- 유지한 테스트:
- 실행 명령:
- 결과:

### Learning Points

- 배운 점:
- 다음에도 적용할 기준:
- 피해야 할 기준:

### Trade-Offs

- 좋아진 점:
- 비용이 늘어난 점:
- 아직 불확실한 점:

### Next Actions

- 다음 단계에서 이어갈 일:
- 나중에 다시 볼 질문:
```

## Step 0. Project Bootstrap

### Date

- 2026-05-18

### Commit

- `f6d97b9` chore: bootstrap spring kotlin architecture lab

### As-Is

- 변경 전 책임 배치: 프로젝트가 없어서 학습할 코드 기준이 없었다.
- 변경 전 의존성 방향: 아직 정의되지 않았다.
- 변경 전 테스트 상태: 테스트 실행 기준이 없었다.
- 변경 전 변경 비용: 새 기능을 추가할 기준 패키지와 실행 방법이 없었다.

### Problem

- Spring Boot, Kotlin, Gradle 기반의 일관된 학습 환경이 필요했다.
- `payment`, `notification`, `auth` 세 도메인을 같은 방식으로 비교할 출발점이 필요했다.

### To-Be

- 변경 후 책임 배치: 각 도메인에 Controller, Service, Strategy 구현체를 둔 단순 출발점을 만든다.
- 변경 후 의존성 방향: 아직 엄격한 레이어 분리는 없지만 전략 인터페이스를 통해 구현체 교체 지점을 만든다.
- 변경 후 테스트 상태: Spring context 테스트와 API smoke test를 실행할 수 있다.
- 기대하는 변경 비용: 다음 단계에서 테스트와 레이어 분리를 통해 변경 비용을 측정할 수 있다.

### Changes

- Spring Initializr 기반 Kotlin/Spring Boot 프로젝트를 생성했다.
- `payment`, `notification`, `auth` 샘플 API를 추가했다.
- README와 TODO 로드맵을 추가했다.
- Git 원격 저장소를 연결하고 초기 커밋을 push했다.

### Tests

- 추가한 테스트: API smoke test
- 유지한 테스트: Spring context load test
- 실행 명령: `./gradlew test`
- 결과: 통과

### Learning Points

- 학습용 프로젝트도 실행, 테스트, 문서, Git 이력을 먼저 갖추면 이후 변경을 비교하기 쉽다.
- 처음부터 완성된 구조를 만들기보다 의도적으로 단순한 `As-Is`를 두면 리팩터링 학습에 유리하다.
- Strategy 패턴의 최소 형태를 먼저 두면 이후 Factory, Registry, Adapter 같은 패턴의 필요성을 비교하기 쉽다.

### Trade-Offs

- 좋아진 점: 세 도메인을 같은 방식으로 연습할 수 있는 기준점이 생겼다.
- 비용이 늘어난 점: 아직 Controller 파일에 여러 책임이 모여 있어 구조가 단순하지만 느슨하다.
- 아직 불확실한 점: Boot 4 기반에서 일부 테스트 slice와 문서화 도구의 패키지 변경을 확인해야 한다.

### Next Actions

- Step 1에서 서비스 단위 테스트를 추가한다.
- 실패 케이스 정책을 먼저 테스트로 고정한다.

## Step 0.5. 도메인 베이스라인 정의

### Date

- 2026-05-18

### Commit

- `70a186e` docs: add domain baseline step

### As-Is

- 변경 전 책임 배치: 세 도메인이 Controller, DTO, Service, Strategy 구현체를 한 파일에 함께 가지고 있었다.
- 변경 전 의존성 방향: HTTP, 애플리케이션 흐름, 도메인 규칙 후보가 구분되지 않았다.
- 변경 전 테스트 상태: API smoke test는 있었지만 도메인 실패 정책을 설명하는 테스트 후보가 부족했다.
- 변경 전 변경 비용: 무엇을 테스트하고 리팩터링해야 하는지 판단할 도메인 기준이 약했다.

### Problem

- `payment`, `notification`, `auth`가 샘플 API 수준이라 TDD, DDD, 디자인 패턴을 깊게 연습하기에 도메인 압력이 부족했다.
- 처음부터 아키텍처를 완성하면 before/after 학습 효과가 줄어들지만, 도메인 규칙마저 비어 있으면 리팩터링 방향이 흐려진다.

### To-Be

- 변경 후 책임 배치: 코드는 그대로 두고 문서에서 도메인 개념, 핵심 규칙, 확장 후보를 먼저 정의한다.
- 변경 후 의존성 방향: 아직 코드 의존성은 느슨하지만, 이후 단계에서 분리할 책임의 기준을 문서화한다.
- 변경 후 테스트 상태: Step 1에서 옮길 테스트 후보가 생긴다.
- 기대하는 변경 비용: 이후 변경은 도메인 규칙 유지 여부를 기준으로 비교할 수 있다.

### Changes

- 코드 변경: 없음.
- 패키지/레이어 변경: 없음.
- 설계 패턴 또는 원칙: 도메인 규칙을 먼저 정리하고 아키텍처 미성숙 상태는 학습 재료로 남겼다.
- 문서 변경: `Step 0.5. 도메인 베이스라인 정의` 문서를 추가하고 TODO, Roadmap, Step 1, Step 3 문서에 연결했다.

### Tests

- 추가한 테스트: 없음.
- 유지한 테스트: 기존 API smoke test와 context load test.
- 실행 명령: `git diff --check`
- 결과: 통과.

### Learning Points

- 학습용 As-Is는 도메인이 비어 있는 코드보다, 도메인 규칙은 보이지만 구조가 부족한 코드가 더 좋다.
- TDD를 시작하기 전에 도메인 규칙을 문장으로 정리하면 테스트 이름과 실패 케이스를 고르기 쉽다.
- 아키텍처의 부족함을 의도적으로 남겨야 레이어 분리, DDD, 패턴 적용의 효과를 단계별로 비교할 수 있다.

### Trade-Offs

- 좋아진 점: Step 1에서 테스트로 고정할 정상/실패/상태 변화 시나리오가 분명해졌다.
- 비용이 늘어난 점: 코드에는 아직 규칙이 반영되지 않아 문서와 구현 사이에 의도적인 차이가 있다.
- 아직 불확실한 점: 모든 규칙을 한 번에 구현하면 학습 단위가 커지므로 Step 1에서 우선순위를 다시 정해야 한다.

### Next Actions

- Step 1에서 각 도메인별 성공 케이스와 대표 실패 케이스를 테스트로 먼저 고정한다.
- 코드 구현은 최소 변경으로 두고, 실패 테스트가 표현하는 요구사항을 커밋 단위로 남긴다.

## Step 0.5. E-commerce 도메인 컨텍스트 보강

### Date

- 2026-05-18

### Commit

- `9766098` docs: align baseline with ecommerce domain

### As-Is

- 변경 전 책임 배치: `payment`, `notification`, `auth`의 개별 규칙은 문서화되어 있었지만 e-commerce 주문 흐름 안에서의 역할이 약했다.
- 변경 전 의존성 방향: 주문, 고객, 결제, 알림, 인증의 경계가 코드와 문서 모두에서 충분히 드러나지 않았다.
- 변경 전 테스트 상태: Step 1 테스트 후보가 있었지만 주문 결제, 결제 알림, 고객 인증 시나리오로 묶이지 않았다.
- 변경 전 변경 비용: 이후 단계에서 어떤 주변 도메인을 구현해야 하고 어떤 도메인은 문서상 경계로만 둘지 판단하기 어려웠다.

### Problem

- `payment`, `notification`, `auth`가 기능 단위 샘플처럼 보이면 e-commerce 설계 학습으로 확장하기 어렵다.
- `order`, `cart`, `catalog`, `inventory`, `shipment`를 모두 바로 구현하면 Step 0.5가 너무 커지고 이후 리팩터링 학습 포인트가 흐려진다.

### To-Be

- 변경 후 책임 배치: 초기 구현 범위는 `payment`, `notification`, `auth`로 유지하되, 각 책임을 주문 흐름 안에서 설명한다.
- 변경 후 의존성 방향: 주변 도메인은 구현하지 않고 결제, 알림, 인증의 입력과 이벤트를 이해하기 위한 경계로 문서화한다.
- 변경 후 테스트 상태: Step 1에서 주문 결제, 결제 완료 알림, 고객 인증 happy path와 대표 실패 정책을 고정할 수 있다.
- 기대하는 변경 비용: 주변 도메인을 한 번에 만들지 않고도 e-commerce 규칙을 기준으로 점진적 설계를 진행할 수 있다.

### Changes

- 코드 변경: 없음.
- 패키지/레이어 변경: 없음.
- 설계 패턴 또는 원칙: bounded context 후보와 ubiquitous language를 먼저 정의하고 구현은 이후 단계로 미뤘다.
- 문서 변경: Step 0.5에 e-commerce scope, 주변 도메인, 대표 사용자 흐름, 공통 용어, 테스트 후보를 추가했다.

### Tests

- 추가한 테스트: 없음.
- 유지한 테스트: 기존 API smoke test와 context load test.
- 실행 명령: `git diff --check`
- 결과: 통과.

### Learning Points

- e-commerce에서는 결제, 알림, 인증이 독립 기능이 아니라 주문 흐름의 일부로 의미가 생긴다.
- 모든 주변 도메인을 코드로 먼저 만들지 않아도 용어와 경계를 문서화하면 이후 DDD와 TDD의 방향을 잡을 수 있다.
- 학습용 프로젝트에서는 도메인은 탄탄하게 정의하고 아키텍처는 일부러 미성숙하게 남기는 편이 before/after 비교에 유리하다.

### Trade-Offs

- 좋아진 점: Step 1의 테스트 후보가 더 구체적인 e-commerce 시나리오로 정렬되었다.
- 비용이 늘어난 점: 문서상 도메인 범위가 넓어져 이후 단계에서 우선순위를 계속 관리해야 한다.
- 아직 불확실한 점: `order`를 언제 코드로 도입할지, 또는 `OrderPaymentContext` 같은 최소 모델로 먼저 둘지는 Step 1과 Step 3에서 다시 판단해야 한다.

### Next Actions

- Step 1에서 고객 인증, 주문 결제, 결제 완료 알림의 최소 happy path를 테스트로 고정한다.
- 실패 케이스는 한 번에 모두 구현하지 않고 각 도메인별 대표 케이스부터 Red, Green, Refactor 순서로 진행한다.

## Step 0.5. 코드 베이스라인 잠금

### Date

- 2026-05-19

### Commit

- `67c39a7` docs: lock step 0.5 code baseline

### As-Is

- 변경 전 책임 배치: e-commerce 규칙과 주변 도메인 정의는 있었지만, 현재 코드가 어떤 부족한 상태로 남아 있는지 파일 단위 스냅샷은 약했다.
- 변경 전 의존성 방향: Controller, DTO, Service, Strategy 구현체가 한 파일에 섞여 있는 상태를 학습용 As-Is로 명시적으로 고정하지 않았다.
- 변경 전 테스트 상태: Step 1 테스트 후보는 많았지만 처음부터 어디까지 테스트할지 우선순위가 더 필요했다.
- 변경 전 변경 비용: Step 1에서 도메인 규칙을 과하게 구현할 위험이 있었다.

### Problem

- Step 0.5는 도메인을 탄탄하게 만들되 아키텍처는 일부러 미성숙하게 남기는 단계다.
- 이 의도를 명확히 기록하지 않으면, Step 1 전에 `Order`, `Customer`, `PaymentAttempt` 같은 모델을 성급하게 구현할 수 있다.

### To-Be

- 변경 후 책임 배치: 코드 변경 없이 현재 파일별 책임 과잉 상태를 As-Is로 잠근다.
- 변경 후 의존성 방향: HTTP, Application, Domain, Infrastructure가 아직 섞여 있음을 다음 단계의 분리 대상으로 남긴다.
- 변경 후 테스트 상태: Step 1에서는 happy path, Strategy 선택, validation, 대표 실패 케이스만 먼저 다룬다.
- 기대하는 변경 비용: 학습 범위를 작게 유지하면서도 e-commerce 규칙을 기준으로 다음 변경을 진행할 수 있다.

### Changes

- 코드 변경: 없음.
- 패키지/레이어 변경: 없음.
- 설계 패턴 또는 원칙: 성급한 모델링을 미루고, TDD로 변경 압력을 확인한 뒤 구조를 개선한다.
- 문서 변경: Step 0.5에 코드 베이스라인 스냅샷, 코드 변경을 하지 않는 이유, Step 1 우선순위 컷을 추가했다.

### Tests

- 추가한 테스트: 없음.
- 유지한 테스트: 기존 API smoke test와 context load test.
- 실행 명령: `git diff --check`
- 결과: 통과.

### Learning Points

- 도메인 기준선과 코드 기준선은 함께 있어야 이후 변경의 before/after가 선명해진다.
- 학습용 Step 0.5에서는 코드를 더 만들지 않는 것도 중요한 설계 결정이다.
- 테스트 우선순위를 작게 자르면 TDD, DDD, 패턴 적용을 한 단계에 섞지 않을 수 있다.

### Trade-Offs

- 좋아진 점: 다음 단계에서 무엇을 테스트하고 무엇을 미룰지 더 명확해졌다.
- 비용이 늘어난 점: 문서와 코드 사이의 의도적인 간극을 계속 관리해야 한다.
- 아직 불확실한 점: 주문 컨텍스트를 테스트 더블로 둘지, 최소 `OrderPaymentContext`로 둘지는 Step 1에서 결정해야 한다.

### Next Actions

- Step 1 브랜치에서 서비스 단위 테스트를 먼저 추가한다.
- 대표 실패 케이스는 Payment, Notification, Auth에서 각각 하나씩만 골라 Red, Green, Refactor로 진행한다.
