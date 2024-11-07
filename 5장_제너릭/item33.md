# 타입 안전 이종 컨테이너를 고려하라

- 하나의 컨테이너에서 매개 변수화 할 타입의 수가 제한된다.
- Set<E> 하나의 매개 변수 타입, Map<K,V> 두개 매개 변수 타입.

### 이종 컨테이너 패턴
- 컨테이너 대신 키를 매개변화한 다음, 컨테이너에 값은 넣거나 뺄 때 매개변수 키를 함께 제공하면 된다.
- 얻어올 때 Class 객체를 알려주면 된다.

```java
public class Favorites{
    public <T> void putFavorite(Class<T> type, T instance);
    public <T> T getFavorite(Class<T> type);
}

```

```java
public class Favorites{
    private Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), instance);
    }

    public <T> T getFavorite(Class<T> type) {
        return type.cast(favorites.get(type));
    }
}
```
```java
public static void main(String[] args) {
     Favorites f = new Favorites();
        
     f.putFavorite(String.class, "Java");
     f.putFavorite(Integer.class, 0xcafebabe);
     f.putFavorite(Class.class, Favorites.class);
       
     String favoriteString = f.getFavorite(String.class);
     int favoriteInteger = f.getFavorite(Integer.class);
     Class<?> favoriteClass = f.getFavorite(Class.class);
        
     // Java cafebabe Favorites 출력
     System.out.printf("%s %x %s%n", favoriteString,
                favoriteInteger, favoriteClass.getName()); 
 }
```

- Favorites 인스턴스는 타입 안전하다. String을 요청했는데 Integer를 반환하는 일은 절대 없다.
- Map<Class<?>, Object> 와일드카드 타입이 중첩되었다는 점을 깨달아야 한다. Class<String>, Class<Integer>으로 될 수 있다.
- 이 맵은 키와 값 사이의 타입 관계를 보증 하지 않는다. 이 관계가 성립함을 알고 있고, 즐겨찾기를 검색할때 이점 있음.
- getFavorite 코드는 putFavorite보다 강조해두었다. 주어진 Class 객체를 맵에서 꺼낸다 하지만 잘못된 컴파일타임 타입을 가지고 있다. 이객체의
타입은(favorites) Object 이나, 우리는 이를 T로 바꿔 반환해야 한다.
- Class의 cast 메서드를 사용해 이 객체 참조를 Class 객체가 가리키는 타입으로 동적변환한다.

<br>

- cast 메서드
  - 형변환 연산자의 동적 버전으로, 주어진 인수가 Class 객체가 알려주는 타입의 인스턴스인지 검사한다음, 맞다면 반환하고 아니면 `ClassCastException`을 던진다.
    이를 활용하면, `T`로 비검사 형변환을 하지 않아도 된다.
    ```java
        public class Class<T>{
	        T cast(Object obj);
        }
        
    ```
    

### 타입 안정 이종 컨테이너 제약 2가지

1. Class 객체를 제네릭이 아닌 로 타입 으로 넘기면, 타입 안정성이 쉽게 깨진다.
    - 컴파일할 때 비검사 경고가 발생해 런타임에 타입 안정성이 보장된다.
    ```java
    f.putFavorite((Class)Integer.class, "문자열");
    int result = f.getFavorite(Integer.class);  // ClassCastException
    ```
    - 타입 불변식을 어기는 일이 없도록 보장하려면, type으로 명시한 타입과 같인지 확인하면된다.
    ```java
    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), type.cast(instance));
    }
    ```
    - java.util.Collections 에 checkedSet(), checkedList(), checkedMap() 메서드들도 
      해당 방식을 적용한 컬렉션 래퍼들이다. 해당 클래스들은 모두 CheckedCollection 을 상속 받았고, typeCheck 메서드를 통해 추가 연산시에 타입을 체크하여 안전성을 보장한다.
    ```java
    static class CheckedCollection<E> implements Collection<E>, Serializable {
        @java.io.Serial
        private static final long serialVersionUID = 1578914078182001775L;

        @SuppressWarnings("serial") // Conditionally serializable
        final Collection<E> c;
        @SuppressWarnings("serial") // Conditionally serializable
        final Class<E> type;

        @SuppressWarnings("unchecked")
        E typeCheck(Object o) {
            if (o != null && !type.isInstance(o))
                throw new ClassCastException(badElementMsg(o));
            return (E) o;
        }
        ...
   
        public E set(int index, E element) {
            return list.set(index, typeCheck(element));
        }

        public void add(int index, E element) {
            list.add(index, typeCheck(element));
        }
   
        ...
    ```

2. 실체화 불가 타입 에는 사용 할 수 없다.
- List<String> 과 같이 제네릭 타입은 하나의 List.class 를 공유하지 고유의 Class 객체가 없기 때문에, String 이나 String[] 와 다르게 키로 저장하면 컴파일 에러가 날 것이다.
`List<String> = List<Integer> = List.class `
- 저장하고 싶다면, 대안으로 스프링에서는 슈퍼 타입 토큰 즉 ParameterizeTypeReference 라는 클래스로 제공하고 있다.
- TypeReference 은 아래 예제와 같이 주로 Json 파일 입출력 시 변환 과정에서 사용하면 편리하다. List<T> 나 Map<T, T> 형태로 파싱이 필요한 경우가 많기 때문이다.
    ```java
    inline fun <reified T: Any> readFileData(readPath: String): T = run {
        try {
             FileReader(readPath).use {
                 val typeRef = object : TypeReference<T>() {} // TypeReference
                 return objectMapper.readValue(it, typeRef)
             }
        } catch (e: IOException) {
            throw CommonException(ResponseCode.INTERNAL_SERVER_ERROR, e)
        }
    }
    ```

### 한정적 타입 토큰
- 한정적 타입 매개변수( E extends Delayed )나 한정적 와일드카드( ? extends Delayed )을 사용하여 표현 가능한 타입을 제한하는 토큰
- Favorites가 어떤 Class 객체든 받아들이므로 비한정적 타입 토큰이라 할 수 있다. 이 메서드들이 허용하는 타입을 제한하고 싶다면, 한정적 타입 토큰을 활용하자.
    ```java
    // Annotation의 서브타입이어야 한다는 제약 조건을 설정.
    // 대상요소에 달려 있다면 애너테이션을 반환 없으면, null 반환
    public <T extends Annotation> T getAnnotation(Class<T> annotationType);
    ```
  
- Class<?> 같은 한정적 타입 토큰을 받는 메서드에 넘기려면 어떻게 해야 할까?
- Class<? extends Annotation> 으로 형변환 하는 방식은 비검사 경고가 뜰 것이다. 대신, asSubClass 메서드를 통해 이러한 형변환을 안전하고 동적으로 수행할 수 있다.
- asSubClass() : 호출된 인스턴스 자신의 Class 객체를 인수가 명시한 클래스로 형변환한다.(형변환은 자식 -> 부모로만 가능하다)
    ```java
        static Annotation getAnnotation(AnnotatedElement element,
                                        String annotationTypeName) {
             Class<?> annotationType = null; // 비한정적 타입 토큰
             try {
                 annotationType = Class.forName(annotationTypeName);
             } catch (Exception ex) {
                 throw new IllegalArgumentException(ex);
             }
             
             return element.getAnnotation(
                    annotationType.asSubclass(Annotation.class));  //형변환
        }
    ```