# ITEM82) 스레드 안전성 수준을 문서화하라

### 1. 스레드 안전에 대한 명시

- 메서드 선언에 synchronized 한정자를 선언할지는 구현 이슈일 뿐 API에 속하지 않는다. 이것만으로는 그 메서드가 스레드 안전하다고 믿기 어렵다.
- 멀티스레드 환경에서도 API를 안전하게 사용하게 하려면 클래스가 지원하는 스레드 안전성 수준을 정확히 명시해야 한다.

### 2. 스레드 안전성 수준

1. 불변 - @Immutable
    - 클래스의 인스턴스가 마치 상수와 같아서 외부 동기화가 필요없는 수준. 대표적으로 String, Long이 있다.
2. 무조건적 스레드 안전(unconditionally thread-safe) - @ThreadSafe
    - 클래스의 인스턴스는 수정될 수 있으나, 내부에서 충실히 동기화하여 별도의 외부 동기화가 없이 동시에 사용해도 안전한 수준. 대표적으로 AtomicLong, ConcurrentHashMap이 있다.
    - ConcurrentHashMap
3. 조건부 스레드 안전 (conditionally thread-safe) - @ThreadSafe
    - 무조건적 스레드 안전과 같으나, 일부 메서드는 동시에 사용하려면 외부 동기화가 필요하다. Collections.synchronized 래퍼 메서드가 반환한 컬렉션들이 여기 속한다.
4. 스레드 안전하지 않음(not thread-safe) - @NotThreadSafe
    - 이 클래스의 인스턴스는 수정될 수 있다. 동시에 사용하려면 각각의 메서드 호출을 클라이언트가 선택한 외부 동기화 매커니즘으로 감싸야 한다. ArrayList, HashMap 같은 기본 컬렉션이 여기 속함
5. 스레드 적대적(thread-hostile)
    - 이 클래스는 모든 메서드 호출을 외부 동기화로 감싸더라도 멀티스레드 환경에서 안전하지 않다.