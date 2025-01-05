# item80) 스레드보다는 실행자, 태스크, 스트림을 애용하라.

### 실행자에게 실행할 테스크를 넘기는 작업입니다.
- Executors를 사용하면 스레드 풀을 통해 제한된 스레드 자원을 효율적으로 재사용할 수 있습니다.
- newSingleThreadExecutor : 하나의 스레드 작업을 순차적으로 처리합니다. 이전 작업이 완료된 후에 다음작업이 실행됩니다.(소규모 작업)
- newCachedThreadPool : 필요한 만큼 새로운 스레드를 생성하지만, 이전에 생성된 스레드를 재사용합니다. (대규모 작업)
- newScheduledThreadPool : 주기적인 작업이나 일정 시간 후에 실행해야 하는 작업에 사용됩니다. (주기적 작업)

```java
//Runnable 사용법
public static void main(String[] args) {
    
    ExecutorService executor = Executors.newSingleThreadExecutor();

    Runnable task = () -> {
        System.out.println("Runnable Task is running");
    };

    executor.execute(task);

    executor.shutdown();
}

```

```java
// Callable 사용법
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> task = () -> {
            Thread.sleep(2000); // 2초 작업
            return "Task completed!";
        };

        Future<String> future = executor.submit(task);

        System.out.println("Task submitted. Waiting for the result...");

        try {
            // 작업이 완료될 때까지 기다리고 결과를 반환받음
            String result = future.get();
            System.out.println("Result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

```

- Callable은 Runnable의 사촌 격이다. Runnable 과 비교해 값을 반환하고 임의의 예외를 던질 수 있다.

### 포크 조인 테스크(Fork/Join Framework)
- Java에서 병렬 작업을 처리하기 위해 도입된 프레임워크로, 작업을 작은 단위로 분할(Fork)하고 작업이 완료되면 결과를 합병(Join)하는 방식으로 동작합니다.


### 정리
- 스레드를 직접 생성하고 관리하기보다는, 자바에서 제공하는 프레임워크를 사용하라라는 의미이다.