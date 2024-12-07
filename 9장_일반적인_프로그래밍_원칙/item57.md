# item57) 지역변수의 범위를 최소화하라

- 지역변수의 범위를 줄이는 가장 강력한 기법은 역시 `가장 처음 쓰일 때 선언하기`이다.
- 거의 모든 지역변수는 선언과 동시에 초기화해야한다.

- for문은 while 문보다 짧아서 가독성이 좋다.
- 메서드를 작게 유지하고 한 가지 기능에 집중하는 것이다.
- ry-with-resources, for-each문과 같은 구조적 접근 방식을 사용하세요.

### 위 코드에서 reader는 try 블록 내부에서만 유효하며, 범위가 명확하게 제한됩니다.
```java
public class TryWithResourcesExample {
    public static void main(String[] args) {
        // try-with-resources로 범위 최소화
        try (BufferedReader reader = new BufferedReader(new FileReader("example.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 기능분리 명화히 해야한다.
- 안좋은 코드
``` java
public class BadMethod {
    public static void processNumbers() {
        int result = 0; // 변수 범위가 너무 넓음

        for (int i = 0; i < 10; i++) {
            result += i; // 여기서 사용
        }

        System.out.println("Result: " + result); // 여기서도 사용
    }
}

```
- 좋은코드
```java
public class GoodMethod {
    public static void main(String[] args) {
        System.out.println("Result: " + calculateSum());
    }

    private static int calculateSum() { // 범위를 작은 메서드로 제한
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += i;
        }
        return sum;
    }
}
```