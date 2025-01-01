# item79) 과도한 동기화는 피하라

- 응답 불가와 안전 실패를 피하려면 동기화 메서드나 동기화 블록 안에서는 제어를 절대로 클라이언트에 양도하면 안 된다.



```java

// 잘못된 코드 - 동기화 블록 안에서 외계인 메서드를 호출한다.
public class ObservableSet<E> extends ForwardingSet<E> {

    public ObservableSet(Set<E> set) {
        super(set);
    }

    private final List<SetObserver<E>> observers = new ArrayList<>();

    public void addObserver(SetObserver<E> observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }
    
    public boolean removeObserver(SetObserver<E> observer) {
        synchronized (observers) {
            return observers.remove(observer);
        }
    }
    
    private void notifyElementAdded(E element) {
        synchronized (observers) {
            for(SetObserver<E> observer : observers) {
                observer.added(this, element);
            }
        }
    }
    
    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if(added) {
            notifyElementAdded(element);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c) {
            result |= add(element); //notifyElementAdded를 호출
        }
        return result;
    }
}
```

관찰자는 addObserver와 removeObserver 메서드를 호출해 구독을 신청하거나 해지한다. 두 경우 모두 다음 콜백 인터페이스의 인스턴스를 메서드에 건넨다.
```java
@FunctionalInterface
public interface SetObserver<E> {
    //ObservableSet에 원소가 더해지면 호출된다.
    void added(ObservableSet<E> set, E element);
}

```

```java
public static void main(String[] args) {
        //1step        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());

        set.addObserver(new SetObserver<>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if(e == 23) {
                    s.removeObserver(this);
                }
            }
        });

        for(int i = 0; i < 100; i++) {
            set.add(i);
        }
        
}

```
- 예상 - 23까지 출력후 관찰자 자신을 구독해지한 다음 조용히 종료.
- 실제 - 23까지 출력한 다음 ConcurrentModificationException을 던진다. 
  </br> 관찰자의 added 메서드 호출이 이러날 시점이 notifyElementAdded 가 관찰자들의 리스트를 순회하는 도중이기 때문이다.
  </br> 이때 문제가 발생하게 된다. 리스트에서 원소를 제거하려는데, 이 리스트를 순회하는 도중이기 때문에 허용되지 않는 동작으로 인식한다.
  </br> notifyElementAdded 메서드에서 수행하는 순회는 동기화 블록이므로 수정이 일어나지 않도록 보장하지만, 정작 자신이 콜백을 거쳐 되돌아와 수정하는 것까지는 막지 못한다.
- 정리
  1. main에서 set.add()가 호출되면 ObservableSet의 재정의된 add()가 호출된다.
  2. 재정의된 add는 notifyElementAdded()를 호출한다.
  3. notifyElementAdded()에서는 관찰자 목록(List<SetObserver<E>>)을 순회하며 added()를 호출한다.
  4. main에서 익명 함수로 정의한 added가 호출된다. 해당 added는 특정 조건에서 removeObserver 메서드를 호출한다.
  5. 이때, removeObserver()가 호출되면 콜백으로 되돌아와 자신을 수정하는 것을 막지 못하므로 원소가 삭제된다.
  6. notifyElementAdded()에서 순회 중인 동기화 블록에서 ConcurrentModificationException이 발생한다.(동기화가 걸려있음에도 원소가 삭제됨 → 동기화 오류)

```java
// 쓸데없이 백그라운드 스레드를 사용하는 관찰자
public static void main(String[] args) {
    ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());
    set.addObserver(new SetObserver<>() {
        public void added(ObservableSet<Integer> s, Integer e) {
            System.out.println(e);
            if(e == 23) {
                ExecutorService exec = Executors.newSingleThreadExecutor();
                try {
                    exec.submit(() -> s.removeObserver(this)).get(); // lock 걸림 - 접근 불가
                    // 메인 스레드는 작업을 기다림
                } catch(ExecutionException | InterruptedException ex) {
                    throw new AssertionError(ex);
                } finally {
                    exec.shutdown();
                }
            }
        }
    });

    for(int i = 0; i < 100; i++) {
        set.add(i);
    }
}
```

- 교착 상태에 빠진다. s.removeObserver 호출시 관찰자르 잠그려 시도하지만 락을 얻을 수 없다.
  </br> 메인스레드가 이미 락을 잡고 있다. 백그라운드 스레드는 락을 제거하기만 기다리는 중이다.

- 만약 앞선 예제들의 리소스(관찰자)가 일관된 상태가 아닌 임시로 불변식이 깨진 상태라면 어떨까?
- Java의 Lock은 재진입을 허용하므로 교착상태에는 빠지지 않는다. 하지만 예외를 발생시킨 예에서는 외계인 메서드를 호출하는 스레드가 이미 Lock을 획득하고 있고 다음 재진입에서도 Lock을 획득한다. 이는 Lock이 제 역할을 하지 못하는 것을 나타내고 재진입 가능 락으로 인해 응답 불가(교착상태)가 될 상황을 안전 실패(데이터 훼손) 상태로 변질시킬 위험을 나타낸다.

```java
//외계인 메서드를 동기화 블록 바깥으로 옮겼다.
public class ObservableSet<E> extends ForwardingSet<E> {

    public ObservableSet(Set<E> set) {
        super(set);
    }

    private final List<SetObserser<E>> observers = new CopyOnWriteArrayList<>();

    public void addObserver(SetObserver<E> observer) {
        observers.add(observer);
    }
	
    public boolean removeObserver(SetObserver<E> observer) {
        return observers.remove(observer);
    }
	
    public void notifyElementAdded(E element) {
        for (SetObserver<E> observer : observers) {
            observers.added(this, element);
        }
    }
    
    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if(added) {
            notifyElementAdded(element);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c) {
            result |= add(element); //notifyElementAdded를 호출
        }
        return result;
    }
}
```

- 자바의 동시성 컬렉션 라이브러리의 CopyOnWriteArrayList가 정확히 이 목적으로 특별히 설계된 것이다.
  </br> ArrayList를 구현한 클래스로, 항상 깨끗한 복사본을 만들어 수행하도록 구현 했다.끔직히 느리지만 관찰자 리스트 용도로는 적합하다.


### 정리
- 동기화 영역 안에서 작업은 최소한으로 줄이자.
- 합당한 이유가 있을 때만 내부에서 동기화하고, 동기화했는지 여부를 문서에 명확히 밝히자

### 개인생각
- 스프링에서 사용할때 특히 성능때문이라도 가변적인 전역변수를 사용하지 않는다. 그리고 데이터가 예상치 못하게 조작될 수 있다.