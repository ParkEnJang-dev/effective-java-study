# item 63) 문자열 연결은 느리니 주의하라

문자열 연결 연산자로 문자열 n개를 잇는 시간은 n^2에 비례한다.

### 문자열 연결을 잘못 사용한 예
```java
public String statement() {
    String result = "";
    for (int i = 0; i < numItems(); i++) {
        result += lineForItem(i);
    }
    return result;
}
```

- 품목이 많을 경우 이 메서드는 심각하게 느려진다.
- StringBuilder 를 사용하자.

### 정리
- StringBuffer는 StringBuilder와 비교해 성능이 떨어지지만 멀티스레드에 안전하다.
- 하지마 메소드 안에서 StringBuilder를 생성하고 사용하면 멀티스레드에 문제에 대해서 해결 가능하다.