# item29. 이왕이면 제네릭 타입으로 만들라

- 제네릭이 필요한 경우
- 지금 이상태에서의 클라이언트는 스택에서 꺼낸 객체를 형변환 해야 하는데, 이때 런타임 요류가 날 위험이 있다.
```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
  		Object result = elements[--size];
  		element[size] = null;
        return result;
    }

  	public boolean isEmpty() {
  		return size = 0;
  	}
    
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}


```

```java
Stack stack = new Stack();
stack.push("test");
String str = (String) stack.pop();
```

- 클래스 선언에 타입매개 변수 추가. 이때 타입 이름음 보통 E를 사용한다. (item 68)
- 제네릭 스택으로 가는 첫 단계
- push 메서드를 통해 저장된느 원소는 타입은 항상 E다
```java
public class Stack<E> {
    private E[] elements; //Object[] -> E[] 변경, 제네릭 배열 생성 오류를 해결.
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    
    // elements는 push(E)로 넘어온 E 인스턴스만 담는다.
    // elements에 값을 넣을때와 꺼낼때 모두 T 타입을 사용하므로 안전한 코드이다.
    // 형병환이 안전암을 직접증명 @SuppressWarnings 로 경고 숨긴다.
    @SuppressWarnings("unchecked")
    public Stack() {
        //this.elements = new E[DEFAULT_INITIAL_CAPACITY]; //오류 발생, 실체화 불가 타입.
        elements =  (E[]) new Object[DEFAULT_INITIAL_CAPACITY]; //오류 발생, 실체화 불가 타입.
    }
	
    public void push(E e) {
        ensureCapacity();
        elements[this.size++] = e;
    }
	
    public E pop() {
        if(this.size == 0) {
            throw new EmptyStackException();
        }
        E result = elements[--size]; //오류대신 경고 발생 incompatible types 
        elements[size] = null;
        return result;
    }
	
    public boolean isEmpty() {
        return size == 0;
    }
	
    private void ensureCapacity() {
        if(this.elements.length == size) {
            this.elements = Arrays.copyOf(this.elements, this.size * 2 + 1);
        }
    }
}
```
1. 첫번째 방법
- 가독성이 좋음
- 형변환 배열 생성시 한번만 해주면 된다.
- 현업에서 더 선호.
- 런타임 타입이 컴파일타임 타입과 달라 힙 오염을 일으킨다.
```java
elements =  (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
```

2. 두번째 방법
- E result = elements[--size];에서 경고
- 배열에서 원소를 읽을 때 마다 생성 해야 한다.
```java
// push에서 E 타입만 허용하므로 이 형변환은 안전하다.
@SuppressWarnings("unchecked")
E result = (E) elements[--size];
```

`Stack<String> stack = new Stack<>();`
- 위 같이 설정된 경우. 명시적 형변환 (String) 을 수행하지 않으며, 이 형변환이 항상 성공함을 보장한다.

`Stack<Object>`, `Stack<int[]>`, `Stack<List<String>>`
- 배열보다 리스트를 우선하라(item28) 와 모순돼 보인다.
- 어떤 참조 타입으로도 Stack을 만들 수 있다.
- Stack<int> 같이 기본 타입은 사용할 수 없다. 제네릭 타입 근본적인 문제이나, 박싱된 기본 타입(아이템 61)으로 우회 가능하다.

`타입 매개변수에 제약들 두는 제네릭 타입`
- `class DelayQueue<E extends Delayed> implements BlockingQueue<E>` 는 java.util.concurrent.Delayed 의 하위 타입만 받는다는 뜻이다.
  ClassCastException 걱정할 필요가 없다.


