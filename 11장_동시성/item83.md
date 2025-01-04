# ITEM83) 지연 초기화는 신중히 사용하라

### 1. 지연 초기화란?

- 지연 초기화는(lazy initialization)는 필드의 초기화 시점을 그 값이 처음 필요할 때까지 늦추는 기법이다.
- 값이 전혀 쓰이지 않으면 초기화도 결코 일어나지 않는다.
- 정적 필드와 인스턴스 필드 모두 사용 가능
- 주로 최적화 용도로 쓰이지만, 클래스와 인스턴스 초기화 때 발생하는 위험한 순환 문제를 해결하는 효과도 있다.
- 필요할 때까지는 웬만하면 하지 말아야 한다.
    
    → 성능 악화
    

### 2. 대부분의 상황에서 일반적인 초기화가 지연 초기화보다 낫다

- 지연 초기화가 초기화 순환성(initialization circularity)을 깨뜨릴 것 같은면 synchronized를 단 접근자를 사용하는 것이 낫다
- 성능 때문에 정적 필드를 지연 초기화해야 한다면 지연 초기화 홀드 클래스 (lazy initialization holder class) 관용구를 사용하자.
    
    → 클래스는 클래스가 처음 쓰일 때 비로소 초기화되는 특성을 이용한 관용구다.
    
    ```java
    privat static class FieldHolder {
    	static final FieldType field = computeFieldValue();
    }
    
    private static FieldType getField() { return FieldHolder.field;}
    ```
    
    - getField가 처음 호출되는 순간 FieldHolder.field가 처음 읽히며 FieldHolder 클래스 초기화를 촉발한다.
    - 필드에 접근하면서 동기화를 전혀 하지 않으니 성능이 느려지지 않는다.
- 성능 때문에 인스턴스 필드를 지연 초기화해야 한다면 이중검사(double-check) 관용구를 사용하라.
    - 초기화된 필드에 접근할 때 동기화 비용을 없애준다.
    - 필드의 값을 두 번 검사하는 방식으로, 한 번은 동기화 없이 검사하고, 필드가 아직 초기화 전이라면 두 번째는 동기화하여 검사한다.
        
        두 번째 검사에서도 필드가 초기화되지 않았을 때만 필드를 초기화한다.
        
    - 필드가 초기화된 후로는 동기화하지 않으므로 해당 필드는 반드시 volatile로 선언해야 한다.
    
    ```java
    private volatile FieldType field;
    
    private FieldType getField() {
    	FieldType result = field;
    	if (result != null)
    		return result;
    	
    	synchronized(this) {
    		if(field == null)
    			field = computeFieldValue();
    		return field;
    	}
    }
    ```
    
- 이중검사에는 반복해서 초기화해도 상관없는 인스턴스 필드를 지연 초기화해야할 때가 있는데 이런 경우면 이중검사에서 두 번째 검사를 생략할 수 있다.
    
    ```java
    private volatile FieldType field;
    
    private FieldType getField() {
    	FieldType result = field;
    	if(result == null)
    		field = resilt = computeFieldValue();
    	return result;
    }
    ```
    
- 모든 스레드가 필드의 값을 다시 계산해도 상관없고 필드의 타입이 long과 double을 제외한 다른 기본 타입이라면, 단일 검사의 필드 선언에서 volatile 한정자를 없애도 된다.이 관용구는 어떤 환경에서는 필드 접근 속도를 높여주지만, 초기화가 스레드당 최대 한 번 더 이뤄질 수 있다. 아주 이례적인 기법으로 거의 쓰지 않는다.
- 모든 초기화 기법은 기본 타입 필드와 객체 참조 필드 모두에 적용할 수 있다. 이중검사와 단일검사 관용구를 수치 기본 타입 필드에 적용한다면 필드의 값을 null대신 0과 비교하면 된다.