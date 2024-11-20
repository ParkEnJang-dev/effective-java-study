# ITEM44) 표준 함수형 인터페이스를 사용하라

함수 객체를 매개변수로 받는 생성자와 메서드를 더 많이 만들어야 한다.  이때 함수형 매개변수 타입을 올바르게 선택해야 한다.

- 필요한 용도에 맞는 게 있다면, 직접 구현하지 말고 표준 함수형 인터페이스를 활용하라
    
    
    | 인터페이스 | 함수 시그니처 | 예 |
    | --- | --- | --- |
    | UnaryOerator<T> | T apply(T t) | String::toLowerCase() |
    | BinaryOperator<T> | T apply(T t1, T t2) | BigInteger::add |
    | Predicate<T> | boolean test(T t) | Collection::isEmpty |
    | Function<T,R> | R apply(T t) | Arrays::asList |
    | Supplier<T> | T get() | Instant::now |
    | Consumer<T> | void accept(T t) | System.out::println |

- Consumer<T> : 단일 입력 매개변수, 반환값 X
    
    ex) void accept(T t)
    
    ```java
    Consumer<String> printer = str -> System.out.println(str);
    printer.accept("Hello World");
    ```
    

- Function<T,R> : 입력 값을 받아 다른 타입의 결과 값을 반환
    
    ex) R apply(T t)
    
    ```java
    // 문자열 길이 반환 function
    Function<String, Integer> stringLength = str->str.length();
    
    int length = stringLength.apply("Hello world!");
    System.out.println("Length: " + length):
    ```
    

- Predicate<T> :  입력 값을 받아 검사하고 boolean 반환
    
    ex) boolean test(T t)
    
    ```java
    //짝수 여부 판단
    Predicate<Integer> isEven = num -> num%2 == 0;
    
    boolean result = isEven.test(4);
    System.out.println("Is even? " + result);
    ```
    

- Supplier<T> : 매개변수가 없고, 반환값을 생성하는 동작 수행
    
    ex) T get()
    
    ```java
    //현재 시간
    Supplier<Long> currentTime = () -> System.currentTimeMills();
    
    long time = currentTime.get();
    System.out.println("Current time: " + time);
    ```
    

- UnaryOperator<T> : 단일 입력 값을 받아 동일한 타입 값 반환
    
    ex) T apply(T t)
    

- BinaryOperator<T> : 두 개의 입력 값을 동일한 타입의 결과 값을 반환
    
    ex) T apply(T t1, T t2)
    

- 직접 만든 함수형 인터페이스에는 항상 @FunctionalInterface 에너테이션을 사용하