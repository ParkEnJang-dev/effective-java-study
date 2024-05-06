# item 10. equals는 일반 규약을 지켜 재정의하라

### 정리

* float 와 double 을 제외한 기본 타입 필드는 == 연산자로 비교하자.
* float, double 필드는 Float.compare, Double.compare를 사용하자
* Float.equals, Double.equals는 사용하지 말자
* 꼭 필요한 경우가 아미녀 equals를 재정의 하지 말자.

