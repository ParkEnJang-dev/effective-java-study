# ITEM65) 리플렉션보다는 인터페이스를 사용하라

### 리플렉션 기능

- 리플렉션 기능(java.lang.reflect)을 이용하면 프로그램에서 임의의 클래스에 접근 가능
    - Constructor, Method, Filed 인스턴스 접근 가능
    - ex) Method,.invoke 는 어떤 클래스의 어떤 객체가 가진 어떤 메서드라도 호출할 수 있음
- 컴파일 당시에 존재하지 않던 클래스도 이용할 수 있다.

### 단점.

1. 컴파일타임 타입 검사가 주는 이점을 하나도 누릴 수 없다.
2. 리플렉션을 이용하면 코드가 지저분하고 장황해진다.
3. 성능이 떨어진다. → 훨씬 느리다.

> 리플렉션은 아주 제한된 형태로만 사용해야 단점을 피하고 이점만 취할 수 있다.
> 
- 리플렉션은 런타임에 존재하지 않을 수도 있는 다른 클래스, 메서드., 필드와의 의존성을 관리할 때  적합