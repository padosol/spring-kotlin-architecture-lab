# Step 6. 에러 처리와 API 계약 정리

## Status

- [x] Not Started
- [ ] In Progress
- [ ] Done

## Goal

도메인 예외와 API 오류 응답을 분리하고, 클라이언트가 일관되게 해석할 수 있는 오류 계약을 만든다.

## As-Is

- 일부 오류는 `ResponseStatusException`에 의존한다.
- 공통 오류 응답 포맷이 없다.
- API 계약 문서가 README의 curl 예제 수준이다.

## To-Be

- 도메인 예외와 API 오류 응답을 분리한다.
- 공통 error response를 정의한다.
- validation 오류, 미지원 타입, 인증 실패, 외부 연동 실패를 구분한다.

## TODO

- [ ] `GlobalExceptionHandler` 추가
- [ ] `ErrorResponse` 표준화
- [ ] 도메인 예외 추가: `UnsupportedPaymentMethodException`, `AuthenticationFailedException` 등
- [ ] API 오류 테스트 추가
- [ ] OpenAPI 문서 도입 여부 검토

## Change Log Points

- 도메인 예외와 API 응답 변환 지점을 분리한 방식을 기록한다.
- 오류 코드, 메시지, HTTP status를 정한 기준을 남긴다.
- validation 오류와 비즈니스 오류를 테스트에서 어떻게 구분했는지 기록한다.

## Learning Points

- 예외는 던지는 위치보다 해석하고 변환하는 경계가 중요하다는 점을 확인한다.
- API 계약을 표준화하면 클라이언트와 테스트가 안정되는 이유를 경험한다.
- 도메인 예외가 HTTP에 직접 의존하지 않도록 만드는 방식을 익힌다.

## Review Questions

- 같은 종류의 오류가 항상 같은 JSON 구조로 내려가는가?
- 클라이언트가 재시도 가능 오류와 입력 오류를 구분할 수 있는가?
- 오류 응답 포맷 변경 시 테스트가 계약 변경을 감지하는가?

## Done Criteria

- 클라이언트가 오류 원인을 일관된 JSON 형식으로 받을 수 있다.
- 도메인 예외가 HTTP 상태 코드에 직접 의존하지 않는다.

## Learning Log

완료 후 [Learning Log](../LEARNING_LOG.md)에 회고를 추가한다.
