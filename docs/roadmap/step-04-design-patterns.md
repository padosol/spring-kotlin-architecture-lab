# Step 4. 디자인 패턴을 목적별로 적용

## Status

- [x] Not Started
- [ ] In Progress
- [ ] Done

## Goal

변경 가능성이 높은 지점에 디자인 패턴을 적용하고, 적용 전후의 변경 비용과 테스트 난이도를 비교한다. 패턴 이름을 외우는 것이 아니라 패턴이 필요한 조건을 코드로 확인한다.

## As-Is

- Strategy 패턴만 얇게 적용되어 있다.
- 패턴 적용 이유와 대체 설계가 문서화되어 있지 않다.
- 구현체 선택 로직이 `firstOrNull { supports(...) }` 형태로 반복된다.

## To-Be

- 변경 가능성이 높은 지점에 패턴을 적용한다.
- 패턴 적용 전후의 변경 범위와 테스트 난이도를 비교한다.
- 공통 선택 로직은 재사용 가능한 구조로 이동한다.

## Pattern Candidates

| Domain | Pattern | 적용 목적 |
| --- | --- | --- |
| payment | Strategy | 결제 수단별 처리 로직 교체 |
| payment | Factory Method | 결제 수단에 맞는 processor 선택 책임 분리 |
| payment | Chain of Responsibility | 할인, 검증, 한도 확인, 승인 요청을 순차 처리 |
| payment | Template Method | 결제 처리 공통 흐름과 세부 구현 분리 |
| notification | Adapter | 이메일/SMS/Push 외부 SDK 호출 방식 통일 |
| notification | Observer | 결제 완료, 로그인 성공 같은 이벤트에 반응 |
| notification | Decorator | 재시도, 로깅, rate limit, tracing 기능 추가 |
| auth | Strategy | 인증 방식별 인증 로직 교체 |
| auth | Chain of Responsibility | 토큰, 세션, OAuth, API key 인증 순차 시도 |
| auth | Specification | 비밀번호 정책, 계정 잠금, 권한 조건 조합 |
| auth | Proxy | 인증된 사용자만 특정 기능 접근 허용 |

## TODO

- [ ] Strategy 선택 로직을 `ProcessorRegistry` 형태로 추출
- [ ] payment 승인 흐름에 Chain of Responsibility 적용
- [ ] notification 외부 발송 모듈에 Adapter 적용
- [ ] notification 발송에 Decorator로 retry/logging 추가
- [ ] auth 정책 검증에 Specification 적용
- [ ] 각 패턴별 `before.md`, `after.md` 또는 README 섹션 작성

## Change Log Points

- 패턴 적용 전 변경 비용과 적용 후 변경 비용을 비교한다.
- 패턴을 적용한 이유, 선택하지 않은 대안, 제거해도 되는 조건을 기록한다.
- 패턴별로 테스트가 단순해졌는지 복잡해졌는지 확인한다.

## Learning Points

- Strategy, Factory, Adapter, Decorator, Chain of Responsibility, Specification의 적용 기준을 코드로 비교한다.
- 패턴이 목적 없이 늘어나면 유지보수성이 오히려 떨어질 수 있음을 확인한다.
- 확장 지점과 안정 지점을 나누어 설계하는 습관을 만든다.

## Review Questions

- 새 구현체를 추가할 때 기존 유스케이스 코드 변경이 줄었는가?
- 패턴 이름을 모르는 사람이 봐도 흐름을 이해할 수 있는가?
- 이 추상화가 현재 요구사항 대비 너무 이른 추상화는 아닌가?

## Done Criteria

- 새 구현체 추가 시 기존 유스케이스 코드 변경이 최소화된다.
- 패턴을 제거했을 때보다 코드가 읽기 쉽거나 테스트하기 쉬운 이유가 설명된다.
- 패턴 적용 후에도 테스트가 빠르고 명확하다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
