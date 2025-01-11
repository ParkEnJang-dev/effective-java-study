# 자바 직렬화의 대안을 찾으라.


### 역직렬화 폭탄 - 이 스트림의 역직렬화는 영원히 계속된다.

```java
import java.util.HashSet;
import java.util.Set;

static byte[] bomb() {
    Set<Object> root = new HashSet<>();
    Set<Object> s1 = root;
    Set<Object> s2 = new HashSet<>();
    for (int i = 0; i < 100; i++) {
        Set<Object> t1 = new HashSet<>();
        Set<Object> t2 = new HashSet<>();
        t1.add("foo");
        t2.add(t1); s1.add(t2);
        s2.add(t1); s2.add(t2);
        s1 = t1; s2 = t2;
        
    }
    return serialize(root);
}
```
- 이코드는 단 몇개의 객체만 생성해도 스택 깊이 제한에 걸려버린다.
- 직렬화 위험을 회피하는 가장 좋은 방법은 아무것도 역직렬화하지 않는 것이다.
- 블랙리스트 방식보다 화이트 리스트 방식을 사용하라.

### 정리
- 객체 직렬화는 위험하니 사용하지 말자.
- 객체 직렬화 대신 JSON, XML, 프로토콜 버퍼 등을 사용하자.
- 