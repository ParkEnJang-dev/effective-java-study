# itme81) wait와 notify보다는 동시성 유틸리티를 애용하라

- wait와 notify는 올바르게 사용하기가 아주 까다로우니 고수준 동시성 유틸리티를 사용하자.

### 고수준 유틸리티
- 실행자 프레임워크
- 동시성 컬렉션(concurrent collection)
  - List, Queue, Map 같은 표준 컬렉션 인터페이스에 동시성을 가미해 구현한 고성능 컬렉션.
  - 동시성을 무력화하는 건 불가능, 외부에서 락을 추가로 사용하면 속도가 느려진다.
- 동기화 장치


### Map의 putIfAbsent(key, value)
- 주어진 키에 매핑된 값이 없을 때만 새 값을 집어 넣는다. 
기존값이 있으면, 기존값을 반환하고 없으면 null을 반환 합니다.
- 이 메서드 덕에 안정한 정규화 맵을 쉽게 구현할 수 있다.

### String.intern 동작 흉내 내어 구현한 메서드

```java

//최적은 아니다
private static final ConcurrentMap<String, String> map =
        new ConcurrentHashMap<>();

public static String intern(String s) {
    String previousValue = map.putIfAbsent(s,s);
    return previousValue == null ? s : previousValue;
}

// 더 빠른 방법
public static String intern(String s) {
    String result = map.get(s);
    if (result == null){
        result = map.putIfAbsent(s,s);
        if (result==null)
            result = s;
    }
    return result;
}

```
- String.intern보다 6배 빠르다.(하지만 String.intern은 메모리 방지기술도 있다)
- 동시성 컬렉션은 동기화한 컬렉션을 낡은 유산으로 만들어버렸다.
- Collections.synchronizedMap보다는 ConcurrentHashMap을 이용하는 게 훨씬 좋다.

### 동시 실행 시간을 재는 간단한 프레임워크
```java
public static long time(Executor executor, int concurrency, 
			Runnable action) throws InturruptedException {
	
    CountDownLatch ready = new CountDownLatch(concurrenycy);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(concurrency);

	for (int i = 0; i < concurrency; i++) {
    	executor.execute(() -> {
        	// 타이머에게 준비를 마쳤음을 알린다.
            ready.countDown();
            try {
            	// 모든 작업자 스레드가 준비될 때까지 기다린다.
                start.await();
                action.run();
            } catch (InturruptedException e){
            	Thread.currentThread().interrupt();
            } finally {
            	// 타이머에게 작업을 마쳤음을 알린다.
                done.countDown();
            }
        });
    }
    
    ready.await(); // 모든 작업자가 준비될 때까지 기다린다.
    long startNanos = System.nanoTime();
    start.countDown(); // 작업자들을 깨운다.
    done.await(); // 모든 작업자가 일을 끝마치기를 기다린다.
    return System.nanoTime() - startNanos;
}
```
- 이 코드는 카운트 다운 랜치를 3개 사용한다.
- ready 래치는 작업자 스레드들이 준비가 완료됐음을 타이머 스레드에게 통지할 때 사용한다. 통지를 끝낸 작업자 스레드들은 두 번째 래치인 start가 열리기를 기다린다. 
- 마지막 작업자 스레드가 ready.countDown을 호출하면 타이머 스레드가 시작 시각을 기록하고 start.countDown()을 호출해 기다리던 작업자 스레드들을 깨운다. 그 직후 타이머 스레드는 세 번째 래치인 done이 열리기를 기다린다. 
- done 래치는 마지막 남은 작업자 스레드가 동작을 마치고 done.countDown을 호출하면 열린다. 타이머 스레드를 done 래치가 열리자마자 깨어나 종료 시각을 기록한다. 
- time메서드에 넘겨진 실행자는 concurrency 매개변수로 지정한 동시성 수준만큼의 스레드를 생성할 수 있어야 한다. 그렇지 못하면 이 메서드는 영원히 끝나지 않는다. 스레드의 수가 concurrency 보다 적어 스레드들은 영원히 대기하게 되기 때문이다. 이런 상태를 스레드 기아 교착상태 (thread starvation deadlock)이라고 한다.

### wait 과 notify
새로운 코드라면 wait과 notify 대신 동시성 유틸리티를 사용해야 한다. 하지만 레거시를 다뤄야 할 때도 있을 것이다.

### wait 메서드를 사용하는 표준 방식
``` java
synchronized (obj) {
	while(<조건이 충족되지 않았다>)
    	obj.wait(); // 락을 놓고, 깨어나면 다시 잡는다.
	
    ... // 조건이 충족되면 동작을 수행한다.
}

```

### notify 대신 notifyAll
일반적으로는 notify 보다는 notifyAll을 호출하는 것이 합리적이고 안전한 조언이 된다.

### 정리
- await, notify 를 직접사용하지 말고 프레임 워크를 이용해 사용하라.
- 레거시 코드를 유지보수 한다면, 표준 관용구에 따라 while 안에서 호출하라.
- notify 보다 notifyAll. notify 를 사용한다면 응답불가 상태 빠지지 않도록 조심히라