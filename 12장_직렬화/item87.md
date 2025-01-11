# item87) 커스텀 직렬화 형태를 고려해보라



### 기본 직렬화 형태에 적합한 후보
---
``` java
public class Name implements Serializable {
 /**
  * 성. null이 아니어야 함.
  * @serial
  */
  private final String lastName;
 /**
  * 이름. null이 아니어야 함.
  * @serial
  */
  private final String firstName;

 /**
  * 중간이름. 중간이름이 없다면 null.
  * @serial
  */
  private final String middleName;

  ... // 나머지 코드는 생략

}

```

- 기본 직렬화 형태가 적합하다고 결정했더라도 불변식 보장과 보안을 위해 readObject 메서드를 제공해야 할 때가 많다.
- private 필드의 설명을 API 문서에 포함하라고 자바독에 알려주는 역할은 @serial 태그가 한다.

### 기본 직렬화 형태에 적합하지 않은 클래스
``` java 
public final class StringList implements Seriallizable {
	private int size = 0;
	private Entry head = null;
	private static class Entry implements Serializable {
		String data;
		Entry next;
		Entry previous;
	}
	
} 

```

객체의 물리적 표현과 논리적 표현의 차이가 클 때 기본 직렬화 형태를 사용하면 크게 네 가지 면에서 문제가 생긴다

1. 공개 API가 현재의 내부 표현 방식에 영구히 묶인다. 기본 직렬화가 객체의 내부 구조를 외부에 그대로 노출하여, 클래스의 설계와 구현에 심각한 제약을 가하게 된다.
2. 너무 많은 공간을 차지할 수 있다.
3. 시간이 너무 많이 걸릴 수 있다.
4. 스택 오버플로를 일으킬 수 있다.

### 합리적인 커스텀 직렬화 형태를 갖춘 StringList
---


``` java
public final class StringList implements Serializable {
    private transient int size = 0;
    private transient Entry head = null;

    // 이제는 직렬화 하지 않는다.
    private static class Entry {
        String data;
        Entry next;
        Entry previous;
    }

    // 지정한 문자열을 이 리스트에 추가한다.
    public final void add(String s) { ... }

		// StringList 인스턴스를 직렬화한다.
    private void writeObject(ObjectOutputStream s)
            throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);

        // 모든 원소를 올바른 순서대로 기록한다.
        for (Entry e = head; e != null; e = e.next) {
            s.writeObject(e.data);
        }
    }

    private void readObject(ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int numElements = s.readInt();

        for (int i = 0; i < numElements; i++) {
            add((String) s.readObject());
        }
    }
    // ... 생략
}
```

- StringList의 필드 모두가 transient라도 writeObject, readObject는 각각 먼저 defaultWriteObject, defaultReadObject를 호출한다.
- 클래스의 인스턴스 필드 모두가 transient면 defaultWriteObject, defaultReadObject를 호출하지 않아도 된다고 들었을지 모르지만, 직렬화 명세는 이 작업을 무조건 하라고 요구한다.
- 그래야 transient가 아닌 인스턴스 필드가 추가된 다음 릴리스에서도 상호 호환되기 때문이다.
- 구버전 readObject 메서드에서 defaultReadObject를 호출하지 않는다면 역직렬화할 때 StreamCorruptedException이 발생할 것이다.
- 논리적 상태와 무관한 필드라고 확신할때만 transient 한정자를 생략해야 한다.


### 동기화 메커니즘 직렬화

``` java
private synchronized void writeObject(ObjectOutputStream stream) throws IOException { 
stream.defaultWriteObject(); 
}

```

- 기본 직렬화 사용 여부와 상관없이 객체의 전체 상태를 읽는 메서드에 적용해야 하는 동기화 메커니즘을 직렬화에도 적용해야 한다.
- 따라서 에컨대 모든 메서드를 synchronized로 선언하여 스레드 안전하게 만든 객체에서 기본 직렬화를 사용하려면 writeObject도 다음 코드처럼 synchronized로 선언해야 한다.


### uid 예시

``` java
import java.io.Serializable;

  

class User implements Serializable {
    private String name;
    private int age;
    //private String email; // 새 필드 추가
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age + "}";
    }

}

//직렬화 
public class SerializeExample {
    public static void main(String[] args) throws IOException {
        User user = new User("Alice", 30);
        // 객체를 파일에 저장
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
            oos.writeObject(user);
            System.out.println("직렬화 완료: " + user);
        }
    }
}
//user.ser 파일에 직렬화된 객체가 저장됩니다.
//시간이 지나 email 필드 추가.

// 역 직렬화
public class DeserializeExample {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // 이전에 저장된 객체 읽기
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user.ser"))) {
            User user = (User) ois.readObject();
            System.out.println("역직렬화 완료: " + user);
        }
    }
}



```

- 역직렬화 시 클래스 구조가 변경되어 에러를 뱉어냄.
``` java

Exception in thread "main" java.io.InvalidClassException:
User; local class incompatible:
stream classdesc serialVersionUID = -1234567890123456789,
local class serialVersionUID = 1234567890123456789

```

* serialVersionUID**를 설정하면 직렬화와 관련된 예외를 예방하고 데이터 호환성을 유지**할 수 있습니다.