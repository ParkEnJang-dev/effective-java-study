# ITEM72) 표준 예외를 사용하라

### 1. 표준 예외 재사용 이점

- 가독성 좋고, 사용하기 쉬움

### 2. 자주 재사용되는 예외들

<aside>
💡

Exception, RuntimeException, Throwable, Error 는 직접 재사용하지 말자.

해당 클래스들을 추상 클래스라고 생각하기.

</aside>

- IllegalArgumentException : 허용되지 않는 값이 인수로 건네졌을 때 (null은 따로처리)
- IllegalStateException : 객체가 메서드를 수행하기에 적절하지 않은 상태일 때
- NullPointerException : null을 허용하지 않는 메서드에 null을 건넸을 때
- IndexOutOfBoundsException : 인덱스가 범위를 넘어섰을 때
- ConcurrentModificationException : 허용하지 않는 동시 수정이 발견됐을 때
- UnsupportedOperationException : 호출한 메서드를 지원하지 않을