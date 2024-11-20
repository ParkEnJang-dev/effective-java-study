# ITEM43) 람다보다는 메서드 참조를 사용하라

- 메서드 참조(method reference) : 람다보다 더 간결
    
    ```java
    map.merge(key, 1, (count, incr) -> count + incr);
    ```
    
    ```java
    map.mergre(key, 1, Integer::sum);
    ```
    
- 매개변수의 이름 자체가 좋은 가이드가 되는 경우 → 길이는 길지만, 메서드 참조보다 가독성 좋고, 유지보수 쉬울 수 있음
- 람다로 할 수 없으면 메서드 참조로도 할 수 없다.
- 주로 메서드와 람다가 같은 클래스에 있을 때 람다가 더 간결할 때가 있다.
    
    ```java
    // 메서드 참조
    service.execute(GoshThisClassNameIsHumonouse::action);
    
    // 람다
    service.execute(() -> action());
    ```
    
- 메서드 참조 유형
    - 정적 메서드를 가리키는 메서드 참조
    - 인스턴스 메서드를 참조하는 유형
        - 수신 객체(receiving object: 참조 대상 인스턴스)를 특정하는 한정적 인스턴스 메서드 참조
            - 함수 객체가 받는 인수와 참조되는 메서드가 받는 인수가 같음
        - 수신 객체를 특정하지 않는 비한정적 인스턴스 메서드 참조
            - 함수 객체를 적용하는 시점에 수신 객체를 알려준다.
            - 주로 스트림 파이프라인에서의 매핑과 필터 함수에 쓰인다.
        - 클래스 생성자를 가리키는 메서드 참조
        - 배열 생성자를 가리키는 메서드 참조

| 정적 | Integer::parseInt | str → Integer.parseInt(str) |
| --- | --- | --- |
| 한정적 | Integer.now()::isAfter | Instant then = Instant.now();
t → then.isAfter(t) |
| 비한정적 | String::toLowerCase | str → str.toLowerCase() |
| 클래스 생성자 | TreeMap<K,V>::new | () → new TreeMap<K,V>() |
| 배열 생성자 | int[]::new | len → new int[len] |

<aside>
💡

메서드 참조 쪽이 짧고 명확하면 쓰고, 아니면 람다 써라

</aside>

- 람다 X → 메서드 참조로는 가능한 유일한 예
    - 제네릭 함수 타임 (generic function type)
        
        → 제네릭 람다식이라는 문법이 존재하지 않기 때문
        
    
    ```java
    interface G1 {
    	<E extends Exception> Object m() throws E;
    }
    interface G2 {
    	<F extends Exception> String m() throws Exception;
    }
    interface G extends G1, G2 {}
    
    //함수형 인터페이스 G를 함수 타입으로 표현
    <F extends Exception> () -> String thorws F
    ```