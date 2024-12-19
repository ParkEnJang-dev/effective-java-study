# item68) 일반적으로 통용되는 명명 규칙을 따르라.

1. 패키지 명명 규칙
- 소문자로 작성합니다.
- 일반적으로 도메인 이름을 거꾸로 사용하는 방식으로 패키지를 작성합니다. 
  - 예: com.example.myapp 
  - 도메인: example.com → 패키지: com.example

2. 클래스 및 인터페이스 명명 규칙 
- 대문자로 시작하고, 단어를 이어 붙일 때 **카멜 케이스(CamelCase)**를 사용합니다.
- 명사는 클래스와 인터페이스의 이름에 어울립니다. 
  - 클래스: ArrayList, HashMap, String 
  - 인터페이스: Serializable, Comparable

3. 메서드 명명 규칙
- 소문자로 시작하고, 단어를 이어 붙일 때 카멜 케이스를 사용합니다.
- 동사 또는 동사구로 메서드 이름을 작성합니다.
  - 동작을 나타냄: add, remove, getName
  - 조건을 확인하는 메서드: isEmpty, hasNext
  - 객체를 반환하는 메서드: toString, toArray

4. 변수 명명 규칙
- 소문자로 시작하고, 단어를 이어 붙일 때 카멜 케이스를 사용합니다.
- 이름은 의미를 명확히 전달해야 합니다.
  - 지역 변수: index, count, bufferSize
  - 인스턴스 변수: size, name, height
  - 상수 변수: static final 필드는 모두 대문자로 작성하며, 단어는 **언더스코어(_)**로 구분합니다.
  - 예: MAX_VALUE, DEFAULT_TIMEOUT

5. 열거 타입(enum) 명명 규칙
- 열거 타입의 상수는 모두 대문자로 작성하며, 단어는 **언더스코어(_)**로 구분합니다.
   ```java
   public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
   }
   ```

6. 제네릭 타입 매개변수 명명 규칙
- 단일 문자로 표현하며, 일반적으로 다음과 같은 명칭을 사용합니다.
  - T: Type
  - E: Element
  - K: Key
  - V: Value
  - N: Number