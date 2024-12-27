# item73) 추상화 수준에 맞는 예외를 던져라.

### 예외 번역()
- 저수준 예외는 상위 계층에서 잡아 자신의 추상화 수준에 맞는 예외로 바꿔 던져야 한다. 

```java
try{
        ...// 저 수준 추상화를 이용한다.
} catch (LowerLevelException e){
    //추상화 수준에 맞게 번역한다.
    throw new HigherLevelException(...);
}

```

- 예외 번역을 사용하는 List<E> 의 get 메서드 명세에 명시된 필수사항임을 기억해두자.

```java
import java.util.ListIterator;
import java.util.NoSuchElementException;

public E get(int index) {
    ListIterator<E> i = listIterator(index);
    try {
        return i.next();
    } catch (NoSuchElementException e) {
        throw new IndexOutOfBoundsException("인덱스 : "+ index);

    }
}
```

### 예외 연쇄(exception chaining)
```java
try {

}catch (LowerLevelException cause){
    // 저수준 예외를 고수준 예외에 실어 보낸다.
    throw new HigherLevelException(cause);
        }
```

- 예외 연쇄용 생성자
- 남용해서는 안된다.
```java
class HigherLevelException extends Exception{
    HigherLevelException(Throwable cause){
        super(cause);
    }
}

```

### 정리
- 아래계층의 예외를 피할수 없다면 API 호출자에 까지 전파하지 않는 방법을 사용. java.util.logging 같은 적절한 로깅 기능을 활용해야 된다.
- 예외 연쇄를 이용하면 상위계층에는 맥락에 어울리는 고수준 예외를 던지면서 근본 원인도 함께 알려주어 오류를 분석하기에 좋다. [item75](./item75.md)