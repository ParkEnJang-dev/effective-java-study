# item52) 다중정의는 신중히 사용하라

- 코드 52-1 대체
```java
public class OverloadingExample {
    public static class Animal {}
    public static class Dog extends Animal {}

    public void greet(Animal animal) {
        System.out.println("Hello, Animal!");
    }

    public void greet(Dog dog) {
        System.out.println("Hello, Dog!");
    }

    public static void main(String[] args) {
        OverloadingExample example = new OverloadingExample();
        Animal myDog = new Dog();
        
        example.greet(myDog); // 출력: "Hello, Animal!"
    }
}

```

- greet 메서드는 컴파일 시점에 호출될 메서드를 결정한다.
- myDog의 컴파일 타입은 Animal이기 때문에 greet(Animal)이 호출됩니다.

```java
public class OverridingExample {
    public static class Animal {
        public void greet() {
            System.out.println("Hello, Animal!");
        }
    }

    public static class Dog extends Animal {
        @Override
        public void greet() {
            System.out.println("Hello, Dog!");
        }
    }

    public static void main(String[] args) {
        Animal myDog = new Dog();
        myDog.greet(); // 출력: "Hello, Dog!"
    }
}
```
- greet 메서드는 런타임에 객체의 실제 타입에 따라 호출됩니다.

### 재정의된 메서드 호출 메커니즘
```java
class Wine {
    String name() { return "포도주"; }
}

class SparklingWine extends Wine {
    @Override String name() { return "발포성 포도주"; }
}

class Champagne extends SparklingWine {
    @Override String name() { return "샴페인"; }
}

public class Overriding {
    public static void main(String[] args) {
        List<Wine> wineList = List.of(
                new Wine(), new SparklingWine(), new Champagne());

        for (Wine wine : wineList)
            System.out.println(wine.name());
    }
}

```
- 안전하고 보수적으로 가려면 매개변수 수가 같은 다중정의는 만들지 말자.
- 가변인수를 사용하는 메서드라면 다중정의를 아예 하지 말아야 한다.(아이템 53)
- 다중정의 대신 메서드 이름을 다르게 지어주자.
  - writeBoolean(boolean), writeInt(int), writeLong(long)
  - readBoolean(), readInt(), readLong() ObjectInputStream 클래스의 read 메서드는 이렇게 되어 있다.

### 오토 박싱 때문에 발생하는 문제
```java
public class SetList {
    public static void main(String[] args) {
      Set<Integer> set = new TreeSet<>();
      List<Integer> list = new ArrayList<>();

      for (int i = -3; i < 3; i++) {
        set.add(i);
        list.add(i);
      }

      for (int i = 0; i < 3; i++) {
        set.remove(i);
        list.remove(i);
      }
      System.out.println(set);
      System.out.println(list);
    }

}
```
- set.remove(i)의 시그니처는 remove(Object)이다. 예상대로 동작.
- list.remove(i)의 시그니처는 remove(int)이다. 예상과 다르게 동작. 인덱스로 인식되어 0번째 인덱스가 삭제되고, 1번째 인덱스가 0번째로 이동한다. 그리고 1번째 인덱스가 삭제된다. 그래서 -2, 0, 2가 남는다.
- List<E> 인터페이스 remove(Object) 와 remove(int) 를 다중정의 했기 때문에 발생하는 문제이다.

### 정리
- 다중정의는 피하자.
- 생성자 다중정의를 피할수 없다면, 정적 팩터리 메서드로 대체하자. 
- 헷갈릴 만한 매개변수는 형변환하여 정확한 다중정의 메서드가 선택되도록 해야 한다.