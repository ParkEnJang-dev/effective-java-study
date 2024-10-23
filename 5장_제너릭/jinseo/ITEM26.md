# ITEM26. 로 타입은 사용하지 말라

- 제네릭 클래스 or 제니릭 인터페이스 : 클래스와 인터페이스 선언에 타입 매개변수가  쓰이는 것
- 흔히 쓰는 List 는 원래 List<E>
- 통틀어 제네릭 타입 (generic type)
- raw type : 타입 매개변수가 없는 제네릭 타입
    - List<E> 의 로 타입은 List 다.

```java
private final Collection stamps = ...;
stamps.add(new Coin(...));

for (Iterator i = stamps.iterator(); i.hasNext(); ) {

}
// 오류는 가능한 한 발생 즉시 이상적으로는 컴파일할 때 발견하는 것이 좋다.

private final Collcetion<Stamp> stamps = ...;

```

- 로 타입을 쓰면 제네릭이 안겨주는 안전성과 표현력을 모두 잃게 된다.
- 마이그레이션 호환성을 위해 로 타입을 지원
- List 와  List<Object> 는 다름
    - List : 로타입
    - List<Object> : 모든 타입 허용 한다는 매개변수화 타입 사용
- 비한정적 와일드카드 타입 (unbounded wildcard type)
    - 제네릭 타입을 쓰고 싶지만 실제 타입 매개변수가 무엇인지 신경 쓰고 싶지 않다면 ?를 사용
- class 리터럴에는 로 타입을 써야 한다.