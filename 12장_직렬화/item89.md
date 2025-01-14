# ITEM89) 인스턴스 수를 통제해야 한다면 readResolve보다는 열거 타입을 사용하라

### 1. readResolve를 이용한 인스턴스 통제

- 싱글턴으로 객체 관리
    
    ```java
    public class Elvis {
        public static final Elvis INSTANCE = new Elvis();
    
        private Elvis() { ...  }
    
        public void leaveTheBuilding() { ... }
    }
    ```
    
- 문제
    - 싱글턴 보장 객체이지만 직렬화하면 보장할 수 없다.
        
        →  implement Serializable을 선언하는 순간 싱글턴이 아니게 됨.
        
        → 기본 직렬화를 쓰지 않더라도, 그리고 명시적인 readObject를 제공하더라도 소용없음. 어떤 readObject를 사용하든 이 클래스가 초기화될 때 만들어진 인스턴스와는 별개인 인스턴스를 반환하게 된다.
        
- 해결
    - readResolve 사용
        
        ```java
        private Object readResolve() {
            // 진짜 Elvis를 반환하고, 가짜 Elvis는 가비지 컬렉터에 맡긴다.
              // 기존에 생성된 인스턴스를 반환한다.
            return INSTANCE;
        }
        ```
        
        - 역직렬한 객체는 무시하고 클래스 초기화 때 만들어진 Elvis 인스턴스를 반환한다.
        - readObject가 만들어낸 인스턴스를 다른 것으로 대체할 수 있다. 이때 readObject가 만들어낸 인스턴스는 가비지 컬렉션 대상이 된다.
        - 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야 한다.
        - 싱글턴 유지 가능
        - 그래도 readResolve 메서드가 수행되기 전에 역직렬화된 객체의 참조를 공격할 여지가 있음.
- 문제
    - readResolve를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야한다. 그렇지 않으면 readResolve 메서드가 수행되기 전에 역직렬화된 객체의 참조를 공격할 여지가 남는다.
    - 역직렬화 과정에서 역직렬화 인스턴스를 가지고 올 수 있으며 이는 싱글턴이 깨지게 된다는 점이다.

### 2. Enum 사용

- 문제
    - readResolve 메서드가 수행되기 전에 역직렬화된 객체의 참조를 공격할 여지가 있음.
    - 잘못된 싱글턴 - transient가 아닌 참조 필드를 가지고 있는 경
    
    ```java
    public class Elvis imnplements Serializable {
    	public static final Elvis INSTANCE = new Elvis();
    	private Elvis() { }
    	
    	private String[] favoriteSongs = 
    		{ "Hound Dog", "Heartbreak Hotel" };
    	
    	private Object readResolve() {
    		return INSTANCE;
    	}
    }
    ```
    
- 해결
    - enum 사용
        
        ```java
        public enum Elvis {
            INSTANCE;
        
          private String[] favoriteSongs = {"Hound Dog", "Heartbreak Hotel"};
        
          public void printFavorites() {
            System.out.println(Arrays.toString(favoriteSongs));
          }
        }
        ```
        
        → 직렬화 가능한 인스턴스 통제 클래스를 열거 타입을 이용해 구현하면 선언한 상수 외의 다른 객체는 존재하지 않음을 자바가 보장해준다.
        

### 3. 정리

- 불변식을 지키기 위해 인스턴스를 통제해야 한다면 가능한 한 열거 타입을 사용하자.
- 여의치 않은 상황에서 직렬화와 인스턴스 통제가 모두 필요하다면 readResolve 메서드를 작성해 넣어야 하고, 그 클래스에서 모든 참조 타입 인스턴스 필드를 transient로 선언해야 한다.