# item51) 메서드 시그니처를 신중히 설계하라


- 메서드 이름을 신중히 짓자. 아이템 (68 참고)
- 편의 메서드를 너무 많이 만들지 말자.
- 매개변수 목록은 짧게 유지하자. 4개 이하로.
  - 같은 타입의 매개변수가 여러 개가 연달아 나오는 겨우가 해롭다
  - 매개변수 목록 줄이는 3가지 기술
  1. 여러 메서드로 분리한다. 
  2. 여러 매개변수를 묶어주는 도우미 클래스 생성.
  3. 앞서의 두 기법을 혼합한 것으로, 객체 생성에 사용한 빌더 패턴을 메서드 호출에 응용한다. 메서드를 객체 안에서 만들어 검증하는 로직도 추가할 수 있다.

### 매개변수의 타입으로는 클래스보다는 인터페이스가 더 낫다.
- 인터페이스를 타입으로 선언하면 그 인터페이스를 구현한 클래스의 객체를 받을 수 있게 된다.
- 예를들어, HashMap 을 넘길 일은 전혀 없다. 대신 Map을 넘기자. TreeMap, ConcurrentHashMap, TreeMap의 부분맵등 어떤 Map 구현체든 넘길 수 있다.
- boolean 보다는 원소가 2개인 열거 타입이 낫다.
` public enum TemperatureScale { FAHRENHEIT, CELSIUS }`
- 예제
```java
//지양 방법
public class Door {
    public void setLockState(boolean isLocked) {
        if (isLocked) {
            System.out.println("Door is now locked.");
        } else {
            System.out.println("Door is now unlocked.");
        }
    }

    public static void main(String[] args) {
        Door door = new Door();
        door.setLockState(true);  // true가 무슨 의미인지 코드를 보고 유추해야 함
        door.setLockState(false);
    }
}

//권장 방법
public class Door {
  public enum LockState {
    LOCKED, UNLOCKED
  }

  public void setLockState(LockState state) {
    if (state == LockState.LOCKED) {
      System.out.println("Door is now locked.");
    } else if (state == LockState.UNLOCKED) {
      System.out.println("Door is now unlocked.");
    }
  }

  public static void main(String[] args) {
    Door door = new Door();
    door.setLockState(LockState.LOCKED);  // LOCKED라는 명확한 의미 전달
    door.setLockState(LockState.UNLOCKED);
  }
}

```