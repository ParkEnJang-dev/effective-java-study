# ITEM90) 직렬화된 인스턴스 대신 직렬화 프록시 사용을 검토하라

### 1. 직렬화 프록시 패턴(Serialization Proxy Pattern)

- 직렬화를 원본이 아닌 프록시 객체가 대신 직렬화되고, 역직렬화할때 프록시에서 원본 객체를 반환하여 인스턴스화 하는 기법이다.
- 바깥 클래스의 논리적 상태를 설명하는 중첩 클래스를 private static로 선언하여, 중첩 클래스가 바깥 클래스의 직결화 프록시가 되도록 하면 된다.
    
    ```java
    // 1. 바깥 클래스와 직렬화 프록시 모두 Serializable을 구현해야 한다.
    private static class SerializationProxy implements Serializable {
    	private final Date start;
    	private final Date end;
    	
    	// 2. 생성자는 단 하나, 바깥클래스를 매개변수로 받아야 한다.
    	SerializationProxy(Period period) {
    		this.start = period.start;
    		this.end = period.end;
    	}
    	
    	private static final long serialVersionUID = 1L;
    	
    	// 5. 역직렬화시 직렬화 시스템이 직렬화 프록시를 다시 바깥 클래스의 인스턴스로 변환해준다.
    	private Object readResolve() {
    		return new Period(start, end);
    	}
    }
    
    // 3. 바깥 클래스에 메서드 추가해야 한다.
    //자바의 직렬화 시스템이 바깥 클래스의 인스턴스 대신 직렬화 프록시 인스턴스를 반환한다
    // =>다시말해, 직렬화가 되기 전에 바깥 클래스의 인스턴스를 직렬화 프록시로 변환한다.
    private Object writeReplace() {
    	return new SerializationProxy(this);
    }
    
    // 4. 바깥 클래스에 아래와 같은 readObject 메서드를 추가하여 불변식을 훼손하려는 공격을 막아낼 수 있다.
    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    	throw new InvalidObjectException("프록시가 필요합니다");
    }
    ```
    
- writeReplace() : 직렬화에 간섭하는 메서드
- readResolve() : 역직렬화에 간섭하는 메서드
- 직렬화 프록시 패턴은 가짜 바이트 스트림을 만들어 공격하거나 내부 필드를 탈취하는 공격을 프록시 수준에서 차단해준다.
- 직렬화 프록시에서는 Period의 필드를 final로 선언해도 되므로, 진정한 불변 객체로 만들 수 있다.
- 이런 이유들로 어떤 필드가 공격의 목표가 될 지 고민하지 않아도 되고, 역직렬화 때 유효성 검사를 할 필요도 없다.
- 직렬화 프록시 패턴은 역직렬화한 인스턴스와 원래의 직렬호된 인스턴스의 클래스가 달라도 정상 작동한다.
    
    예시) EnumSet의 직렬화 프록시
    
    ```java
    private static class SerializationProxy<E extends Enum<E>> implements Serializable {
    	// EnumSet의 원소 타입
    	private final class<E> elementType;
    	// EnumSet의 원소들
    	private final Enum<?>[] elements;
    	
    	SerializationProxy(EnumSet<E> set) {
    		elementType = set.elementType;
    		elements = set.toArray(new Enum<?>[0]);
    	}
    	
    	private Object readResolve() {
    		EnumSet<E> result = EnumSet.noneOf(elementType);
    		for(Enum<?> e : elements)
    			result.add((E) e));
    		return result;
    	}
    	
    	private static final long serialVersionUID = 1L;
    }
    ```
    

### 2. 직렬화 프록시의 한계

- 클라이언트가 멋대로 확장할 수 있는 클래스는 오버라이드해서 변형시킬 수 있으니 적용할 수 없다.
- 객체 그래프에 순환이 있는 클래스에 적용할 수 없다.
    - 순환이 있는 객체의 메서드를 직렬화 프록시의 readResolve 안에서 호출하면, 직렬화 프록시만 있고 아직 실체 객체가 없기 때문에 ClassCastException이 발생할 것이다.
- 직렬화 프록시 패턴을 적용하면 방어적 복사보다 성능이 저하된다.

<aside>
💡

제 3자가 확장할 수 없는 클래스라면 가능한 한 직렬화 프록시 패턴을 사용하자. 이 패턴이 아마도 중요한 불변식을 안정적으로 직렬화해주는 가장 쉬운 방법일 것이다.

</aside>