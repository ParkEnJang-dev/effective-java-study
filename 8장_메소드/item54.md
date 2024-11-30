# ITEM54) null이 아닌, 빈 컬렉션이나 배열을 반환하라

```java
List<CHeese> cheeses = shop.getCheeses();
if (cheeses != null && cheeses.contains(Cheese.STILTON))
	System.out.println("좋았어, 바로 그거야");

```

- 빈 컬렉션을 반환하는 올바른 예

```java
public List<Cheese> getCHeeses() {
	return new ArrayList<>(cheesesInstock;
}
```

- 사용 패턴에 따라 빈 컬렉션 할당이 성능을 눈에 띄게 떨어뜨릴 수도 있음, 해법 간단

```java
public List<Cheese> getCheeses() {
	return cheesesIntock.isEmpty() ? Collections.emptyList()
		: new ArrayList<> (cheesesInstock);
}
```

- 배열을 쓸 때도 마찬가지로 절대 null을 반환하지 말고 길이가 0인 배열을 반환하라.

```java
public Cheese[] getCheeses() {
	return cheesesInStock.toArray(new CHeese[0]);
}
```

- 최적화 - 빈 배열을 매번 새로 할당하지 않도록 함

```java
pricate static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

public Cheese[] getCheeses() {
	return cheesesInStock.toArray(EMPRY_CHEESE_ARRAY);
}
```