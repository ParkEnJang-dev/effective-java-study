# ITEM62) 다른 타입이 적절하다면 사용을 피하라

### 1. 문자열을 쓰지 않아야 할 사례들

- 문자열은 열거 타입을 대신하기에 적합하지 않다.
    
    → 상수를 열거할 때는 문자열보다 열거 타입이 월등히 낫다.
    
- 문자열은 혼합 타입을 대신하기에 적합하지 않다.
    
    ```java
    String com = className + "#" + i.next();
    ```
    
- 문자열은 권한을 표기하기에 적합하지 않다.
    
    ```java
    public class ThredLocal {
    	private ThreadLocal() {}
    	// 스레드 값을 키로 구분해 저장 
    	public static void set(String key, Object value);
    	
    	public static Object get (String key);
    }
    ```
    
    - 의도치 않게 같은 변수를 공유하면 기능 제대로 못함.
    - 보안 취약
    
    → 문자열 대신 위조할 수 없는 키 (권한(capacity)를 사용하여 해결 
    
    ```java
    public final class ThreadLocal<T> {
    	public ThreadLocal();
    	public void set(T value);
    	public T get();
    }
    ```