# item28. 배열보다는 리스트를 사용하라

- 배열
  - 공변(covariant) Sub가 Super의 하위 타입.
    ```java
        Integer[] intArray = {1, 2, 3}; // Integer 는 Number의 하위 타입
        Number[] numArray = intArray;
    ```
- 제네릭
  - 불공변(invariant)
  - 서로 다른 타입이 있을때 하위 타입도 아니고 상위 타입도 아니다.
  - 컴파일시 오류를 바로 알 수 있다.
  - 제릭은 런타임시 타입 정보가 소거 된다. 컴파일시에만 검사하면 런타임시에는 알 수 없다는 뜻이다.
    이러한 점때문에 제네릭으로 순조롭게 전환될 수 있었다.

<br>

- 런타임 시 오류.
  ```java
  Object[] objectArray = new Long[1];
  ObjectArray[0] = "타입이 달라 넣을 수 없다"; //ArrayStoreException을 던진다.
  ```

- 컴파일 시 오류.
  ```java
  List<Object> ol = new ArrayList<Long>();
  ol.add("타입이 달라 넣을 수 없다.");
  ```

 

### 제네릭 배열을 생성을 허용하지 않는 이유
- 타입에 안전하지 않다.허용시 런타임에서 ClassCastException 발생 가능성 있음. 

```java
//허용 가정 하자
List<String>[]stringLists=new List<String>[1];
//원소 하나가 List<Integer>생성
List<Integer> intList=List.of(42);
//string 리스트 배열 Object 배열에 할당.
Object[] objects=stringLists;
//첫 원소로 intList 저장. 제너릭 소거 방식 으로 구현 하여 성공한다.
objects[0] = intlist;
//자동 String 형변환 하는데 ClassCastException 발생
String s = stringLists[0].get(0);
```

### 실체화 불가 타입(non-reifiable type)
- E, List, List 같은 타입을 실체화 불가 타입이라 한다
- 실체화되지 않아서 런타임에는 컴파일 타입보다 타입 정보를 적게 가지는 타입이다.
- 소거 메커니즘 때문에 매개변수화 티입 가운데 실체화될 수 있는 타입은 List<?>와 Map<?,?> 비한정 와일드카드 타입뿐이다.
- 배열을 비한정적 와일드카드 타입으로 만들 수는 있지만, 유용하게 쓰일 일은 거의 없다.

> 배열을 제네릭으로 만들 수 없어 귀찮음.
- 제네릭 컬렉션에서 자신의 원소 타입을 담은 배열을 반환하는게 보통 불가능(해결책 : 아이템 33)
- 제네릭 타입과 가변인수 메서드(varargs method, 아이템53)를 함께 쓰면 해석하기 어려운 경고 메시지 받는다.
- 가변인수 메서드를 호출할 때마다 가변인수 매개변수를 담을 배열이 하나 만들어지는데, 이때 그 배열의 원소가 실체화 불가 타입이라면 경고가 발생하는 것이다.
  (해결책 : 아이템 32)
- 배열로 형변환할 때 제네릭 배열 생성 오류나 비검사 형변환 경고 발생시 대부분 E[] 대신 컬렉션인 List<E>를 사용하면 해결된다.
  코드 복잡도 상승, 성능 하락 될 수 있지만 타입 안정성과 상호운영성이 향상된다.

### 제네릭 배열 적용 예시
- 제네릭 쓰지 않은 예시
- choose 메서드 호출시 Object를 원하는 타입으로 형변화해야 한다. 타입이 다른 원소가 들어 있다면 런타임시 형변환 오류 발생.
```java


public class Chooser {
  private final Object[] choiceArray;

  public Chooser(Collection choices) {
    choiceArray = choices.toArray();
  }

  public Object choose() {
    Random rnd = ThreadLocalRandom.current();
    return choiceArray[rnd.nextInt(choiceArray.length)];
  }
}
```
- 제네릭 만들기 위한 첫시도 컴파일 되지 않는다.(주석)
```java
public class Chooser<T> {
      private final T[] choiceArray;

      public Chooser(Collection choices) {
          //choiceArray = choices.toArray();  
          choiceArray = (T[]) choices.toArray(); 
      }

      public Object choose() {
          Random rnd = ThreadLocalRandom.current();
          return choiceArray[rnd.nextInt(choiceArray.length)];
      }
  }

```
`choiceArray = (T[]) choices.toArray(); `

- 이 경우엔 경고 발생
  - `Unchecked cast: 'java.lang.Object[]' to 'T[]'. Reason: 'choices' has raw type, so result of toArray is erased `
  - T가 무슨 타입인지 알수 없으므로, 이 형변환이 런타임에도 안전한지 보장할 수 없다는 메시지 이다.
  - 안전하다고 확신하면 주석을 달고 애너테이션을 달아 경고를 없애도 되지만, 애초에 경고의 원인을 제거하는게 좋다.

```java
public class Chooser<T> {
      private final T[] choiceArray;

      public Chooser(Collection choices) {
          //choiceArray = choices.toArray();  
          choiceArray = (T[]) choices.toArray(); 
      }

      public Object choose() {
          Random rnd = ThreadLocalRandom.current();
          return choiceArray[rnd.nextInt(choiceArray.length)];
      }
  }

```

### 정리
- 배열은 공변 실체화 반면에 제네릭은 불공변. 
- 둘은 섞어 쓰기란 쉽지 않다. 둘을 썩어 쓰다가 오류를 만나면 배열을 리스트로 대체하는 방법을 적용하자