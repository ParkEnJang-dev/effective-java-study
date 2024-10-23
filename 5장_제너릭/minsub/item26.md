# item26. 로 타입은 사용하지 말라


### 용어 정리
- List<E> E는 제네릭 타입 이라고 한다.
- 제네릭 타입을 클래스에 사용하면 제네릭클랙스, 인터페이스에 사용하면 제네릭 인터페이스다.

| 한글 용어         | 영문 용어                   | 예                                |
|---------------|-------------------------|----------------------------------|
| 매개변수화 타입      | parameterized type      | List<String>                     |
| 실제 타입 매개변수    | actual type parameter   | String                           |
| 제네릭 타입        | generic type            | List<E>                          |
| 정규 타입 매개변수    | formal type parameter   | E                                |
| 비한정적 와일드카드 타입 | unbounded wildcard type | List<?>                          |
| 로 타입          | raw type                | List                             |
| 한정적 타입 매개변수   | bounded type parameter  | <E extends Number>               |
| 재귀적 타입 한정     | recursive type bound    | <T extends Comparable<T>>        |
| 한정적 와일드카드 타입  | bounded wildcard type   | List<? extends Number>           |
| 제네릭 메서드       | generic method          | static <E> List<E> asList(E[] a) |
| 타입 토큰         | type token              | String.class                     |


### 로 타입

```java
List stamps = new ArrayList();
stamps.add(new Coin());
```

```java
for (Iterator i = stamps.iterator(); i.hasNext();){
        Stamp stamp = (Stamp) i.next(); // ClassCastException
        stamp.cancel();
}
```


* 실수로 Stamps 대신 Coin을 넣어도 오류 발생하지 않는다.
* 실행 단계에서 ClassCastException 에러 발생한다. 오류는 컴파일단에서 발생해야 좋다.
* 이전 코드와 호환하기 위한 기능.

### 제네릭

```java
static public void main(String[] args) {

  Collection<Stamp> stamps = new ArrayList<>();

  stamps.add(new Coin()); // error

}
```
* 안정성
  * 컴파일 시점에 오류가 발생하여, 안정성을 확보 한다.

```java
static public void main(String[] args) {

  //List<String> strings = new ArrayList<>();
  List<Object> strings = new ArrayList<>();

  unsafeAdd(strings,Integer.valueOf(42));

  String string = strings.get(0); //  ClassCastException 
}

private static void unsafeAdd(List list, Object o){
  list.add(o);
}

```
* Type 이 Object : 컴파일 에러, String 런타임 에러

### 비한정 와일드 카드
* 매개변수가 무엇인지 신경 쓰고 싶지 않은때 쓴다.
* 로 타입보다 안전하다.
* Collection<?> 불변식 훼손하지 못하게 막았다, null 빼고 어떤 원소도 삽입 불가 하다.

```java
// 로 타입
List rawList = new ArrayList();  // 로우 타입
rawList.add("Hello");
rawList.add(42);  // 다른 타입의 추가 허용

for (Object obj : rawList) {
String str = (String) obj;  // 런타임 시 오류 발생 가능
    System.out.println(str);
}
```
```java
// 비한정 와일드 카드
List<?> wildcardList = new ArrayList<String>();  // 와일드카드 타입
// wildcardList.add("Hello");  // 컴파일 에러 발생, 다른 타입은 추가 불가.

for (Object obj : wildcardList) {
    System.out.println(obj);  // 안전하게 읽기 가능
}
```

### 로 타입 규칙 예외
* 자바 명세 class 리터럴에 매개변수화 타입을 사용하지 못하게 했다.
  * 허용 : List.class, String[].class, int.class
  * 불허 : List<String>.class, List<?>.class
* instanceof 연산자
  * 런타임시 제너릭 정보가 지워진다. 
    그러므로 로 타입이든 비한정적 와일드카드 타입은 똑같이 작동한다.
    로 타입을 쓰는 편이 코드상 깔금하다.
  > 
  > 

