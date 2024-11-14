# @Override 애너테이션을 일관되게 사용하라.
- @Override는 상위 타입의 메서드를 재정의했음을 뜻한다.
- 일관되게 사용하면 여러 가지 악명 높은 버그들을 예방해 준다.


### 영어 알파벳 2개로 구성된 문자열을 표현하는 클래스 - 버그를 찾아보자.

```java
public class Bigram {
    private final char first;
    private final char second;

    public Bigram(char first, char second) {
        this.first  = first;
        this.second = second;
    }

    public boolean equals(Bigram b) {
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        Set<Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new Bigram(ch, ch));
        System.out.println(s.size());
    }
}

```
- main 메서드가 실행되고 Set은 중복을 허용하지 않으니 26이 출력 될 것 같지만, 실제로는 260이 출력된다.
-  equals 메소드를 재정의(overriding)한 게 아니라 다중정의(overloading) 해버린 것이다. 즉 Object의 equals() 를 재정의하려면 매개변수 타입을 Object로 해야 하는데 그렇지 않아서 상속이 아닌 별개의 equals 메서드를 정의한 꼴이 되었다.
```java
// equals 매서드 수정
@Override public boolean equals(Object o) {
    if (!(o instanceof Bigram2))
        return false;
    Bigram2 b = (Bigram2) o;
    return b.first == first && b.second == second;
}
```

구체 클래스에서 상위 클래스의 메서드를 재정의할 때는, 굳이 @Override를 달지 않아도 된다. 구체 클래스인데 구현하지 않은 추상 메서드가 남아 있다면 컴파일러가 자동으로 사실을 알려주기 때문이다.

### 핵심정리
재정의한 모든 메서드에 @Override 애너테이션을 의식적으로 달면, 여러분이 실수했을때 컴파일러가 바로 알려줄 것이다. 예외는 한 가지 뿐이다. 구체 클래스에서 상위 클래스의 추상 메서드를 재정의한 경우에는 이 애너테이션을 달지 않아도 된다.(달아도 상관 없다)