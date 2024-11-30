# ITEM55) 옵셔널 반환은 신중히 하라

- 자바 8 이전 : 메서드가 특정 조건에서 값을 반환할 수 없을 때 예외를 던지거나, null을 반환
- 자바 8 이후 : Optional<T> 추가
- 옵셔널을 반환하는 메서드는 예외를 던지는 메서드보다 유연하고 사용하기 쉬우며, null을 반환하는 메서드보다 오류 가능성이 적다.

```java
public static <E extends Comparable<E>> E max(Collection<E> c) {
	if (c.isEmpty())
		throw new IllegalArgumentException("빈 컬렉션");
	E result = null;
	for (E e : c)
		if(result == null || e.compareTo(result) > 0)
			result = Objects.requireNonNull(e);
	return result;
}
```

→ Optional 수정

```java
public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
	if(c.isEmpty())
		return Optional.empty();
	E result = null;
	for(E e : c)
		if(reuslt == null || e.compareTo(result) > 0)
			result = Objects.requireNonNull(e);
	
	return Optional.of(result);
}
```

→ Optional.of(value)에 null을 넣으면 NullPointerException 발생

→ Optional.ofNullable(value)를 사용하면 null 허용, 하지만 옵셔널을 반환하는 메서드에서는 null을 반환하지 말자.

→ 스트림으로 수정

```java
public static <E extends Comparable<E>> Optional<E> max(Collection<E> c) {
	return c.stream().max(Comparator.naturalOrder());
}
```

- 옵셔널은 검사 예외와 취지가 비슷함 → API사용자에게 반환값이 없을 수도 있음을 명시해줌
- 옵셔널을 반환했을 때 클라이언트는 값을 받지 못했을 때 취할 행동을 선택해야 함.
    1. 기본값을 설정
    
    ```java
    String lastWordInLexicon = max(words).orElse("단어 없음...");
    ```
    
    1. 예외 던지기
    
    ```java
    Toy myToy = max(toys).orElseThrow(TemperTantrumException::new);
    ```
    
    1. 항상 값이 있다 확신하고 곧바로 꺼내기 → 잘못된 판단이면 예외 발생
    
    ```java
    Element = lastNobleGas = max(Elements.NOBLE_GASES).get();
    ```
    
- 기본값 설정 비용이 큰 경우 → Supplier<T>를 인수로 받는 orElseGet을 사용
- isPresent : 옵셔널이 채워져 있으면 true, 아니면 false 반환
    
    → Optional을 Stream으로 변환해주는 어댑터 역할
    
- 컬렉션, 스트림, 배열, 옵셔널 같은 컨테이너 타입은 옵셔널로 감싸면 안된다.
    - 빈 List<T>를 반환하는게 좋음

- 결과가 없을 수 있으며, 클라이언트가 이 상황을 특별하게 처리해야 한다면 Optional<T>를 반환한다.
- 박싱된 기본 타입을 담는 옵셔널은 기본 타입 자체보다 무거울 수밖에 없다 → OptionalInt, OptionalLong, OptionalDouble 을 제공한다.
    
    → 박싱된 기본 타입을 담은 옵셔널을 반환하는 일은 없도록 해야함
    
- 옵셔널을 맵의 값으로 사용하면 절대 안된다.
- 옵셔널을 컬렉션의 키, 값, 원소나 배열의 원소로 사용하는 게 적절한 상황은 거의 없다.

<aside>
💡

옵셔널 반환에는 성능 저하가 따른다.

옵셔널을 반환값 이외의 용도로 쓰는 경우는 매우 드물다.

</aside>