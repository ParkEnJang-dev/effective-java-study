# ordinal 메서드 대신 인스턴스 필드를 사용하라
- 열거 타입 상수는 하나의 정수값에 대응된다.
- ordinal 메소드는 해당 상수가 몇 번째 위치인지를 반환한다.

```java
public enum Ensemble {
    SOLO, DUET, TRIO, QUARTET, QUINTET,
    SEXTET, SEPTET, OCTET, NONET, DECTET;
    
    public int numberOfMusicians(){
        return ordinal() + 1;
    }
    
}

```

- 유지보수가 끔직하다. 상수 선언 순서를 바꾸는 순간 nuberOfMusicians 는 오작동 한다.

```java
public enum Ensembel{

    SOLO(1), DUET(2), TRIO(3)
    ;
    
    private final int numberOfMusicians;
    Ensemble(int size) { this.numberOfMusicians = size;}
    public int numberOfMusicians() { return numberOfMusicians; }
    
}
```

- 이와 같이 정의하자
- ordinal API 문서는 대부분 프로그래머는 이메서드를 쓸 일이 없다. 이 메서드는 EnumSet 과 EnumMap 같이 열거 타입 기반의 범용 자료구조에 쓸 목적으로 설계.
- 그러므로 ordianl 메서드 사용하지 말자.