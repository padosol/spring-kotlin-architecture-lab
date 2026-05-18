# Learning Log

이 문서는 `payment`, `notification`, `auth`를 단계적으로 개선하면서 배운 점을 누적하는 기록장이다. 각 단계가 끝날 때 커밋 해시와 함께 `As-Is`, `To-Be`, 변경 내용, 테스트, 학습 포인트를 남긴다.

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
