# ITEM50) 적시에 방어적 복사본을 만들라

- 클라이언트가 불변식을 깨뜨리려 혈안이 되어 있다고 가정하고 방어적으로 프로그래밍 해야함

```jsx
public final class Period {
	private final Date start;
	private final Date end;
	
	/**
	* @param start 시작 시각
	* @param end 종료 시각; 시작 시각보다 뒤여야 한다.
	* @throws IllegalArgumentException 시작 시각이 종료 시각보다 늦을 때 발생
	
	
```

- 생성자에서 받은 가변 매개변수 각각을 방어적으로 복사(defensive copy)해야 한다.

```java
public Period(Date start, Date end) {
	this.start = new Date(start.getTime());
	this.end = new Date(end.getTime());
	
	if(this.start.compareTo(this.end) > 0)
		throw new IllegalArgumentException(
			this.start + "가" + this.end + "보다 늦다.");
}
```

→ 매개변수의 유효성을 검사하기 전에 방어적 복사본을 만들고, 이 복사본으로 유효성을 검사한 점에 주목

→ 방어적 복사를 매개변수 유효성 검사 전에 수행하면  다른 스레드가 원본 객체를 수정할 위험에서 벗어남

- 매개변수가 제3자에 의해 확장될 수 있는 타입이라면 방어적 복사본을 만들 때 clone을 사용해서는 안된다.
- 접근자가 가변 필드의 방어적 복사본을 반환하면 된다.

```java
public Date start() {
	return new Date(start.getTime());
}
```

- 인스턴스를 복사하는 데는 일반적으로 생성자나 정적 팩터리를 쓰는게 좋다.
- 불변 객체들을 조합해 객체를 구성해야 방어적 복사를 할 일이 줄어든다.

<aside>
💡

클래스가 클라이언트로부터 받는 혹은 클라이언트로 반환하는 구성요소가 가변이면 반드시 방어적 복사해야 함

복사 비용이 너무 크거나 클라이언트가 그 요소를 잘못 수정할 일이 없음을 신뢰한다면 방어적 복사를 수행하는 대신 해당 구성요소를 수정했을 때의 책임이 클라이언트에 있음을 문서에 명시하자.

</aside>