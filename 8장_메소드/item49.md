# ITEM49) 매개변수가 유효한지 검사하라

## 1. 매개변수의 값이 특정 조건을 만족하기를 바람

- 문서화 해야 함
- 메서드 몸체가 시작되기 전에 검사해야 함
- public과 protected 메서드는 매개변수 값이 잘못 됐을 때 던지는 예외를 문서화 해야 함
    - @thorws 자바독 태그를 사용하면 된다
    - 보통은 IllegalArgumentException, IndexOutOfBoundsException, NullPointerException 중 하나가 될 것이다.
    - 제약을 어겼을 때 발생하는 예외도 함께 기술해야 함.

## 2. 예시

- 자바7에 추가된 java.util.requireNonNull 메서드 사용 → 더 이상 null검사를 수동으로 하지 않아도 됨 → 원하는 예외 메시지도 지정 가능
    - 반환값은 무시하고 필요한 곳 어디든 순수한  null 검사 목적으로 사용해도 됨
- 자바 9에서는 Objects에 범위 검사 기능도 더해짐
    - checkFromIndexSize, checkFromToTindex, checkIndex 메서드
        - 리스트와 배열 전용으로 설계
        - 닫힌 범위는 다루지 못함
        - 그래도 유용함

- public이 아닌 메서드라면 단언문(assert)을 사용해 매개변수 유효성을 검증할 수 있음
    
    ```jsx
    private static void sort(long a[], int offset, int length) {
    	assert a != null;
    	assert offset >= 0 && offset <= a.length;
    	assert length >= 0 && length <= a.length - offset;
    	...// 계산 수행
    }
    ```
    
    - 이 단언문들은 자신이 단언한 조건이 무조건 참이라고 선언한다
    - 실패하면 AssertionError 던짐
    - 런타임에 아무런 효과도, 성능 저하도 없음

- 메서드가 직접 사용하지는 않으나 나중에 쓰기 위해 저장하는 매개변수는 특히 더 신경 써서 검사해야 함
    - 클라이언트가 사용하려 할 때 NullPointerException 발생 → 추적 어려움, 디버깅 피곤함

## 3. 예외

1. 유효성 검사 비용이 지나치게 높을 때
2. 실용적이지 않을 때
3. 계산 과정에서 암묵적으로 검사가 수행될 때