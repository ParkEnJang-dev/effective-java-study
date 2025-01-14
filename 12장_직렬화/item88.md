# ITEM88) readObject 메서드는 방어적으로 작성하라

### 1. readObject 메서드 사용

- readObject 메서드가 실질적으로 또 다른 public 생성자이기 때문에 주의해야 함
    - 인수 유효한지 검사
    - 매개변수를 방어적으로 복사해야 한다.
- readObject는 매개변수로 바이트 스트림을 받는 생성자라고 할 수 있다.
- 객체를 역직렬화할 때는 클라이언트가 소유해서는 안 되는 객체 참조를 갖는 필드를 모두 반드시 방어적으로 복사해야 한다.

- 기본 직렬화 형태를 사용한다고 하면 이 클래스의 주요한 불변식을 더는 보장하지 못 함

```java
// 방어적 복사를 사용하는 불변 클래스
public final class Period {
    private final Date start;
    private final Date end;
    
    /**
     * @param start 시작 시각
     * @param end 종료 시각; 시작 시각보다 뒤여야 한다.
     * @throws IllegalArgumentException 시작 시각이 종료 시각보다 늦을 때 발생한다.
     * @throws NullPointerException start나 end가 null이면 발행한다.
     */
    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if(this.start.compareTo(this.end) > 0) {
            throw new IllegalArgumentException(start + "가 " + end + "보다 늦다.");
        }
    }
    
    public Date start() { return new Date(start.getTime()); }
    public Date end() { return new Date(end.getTime()); }
    public String toString() { return start + "-" + end; }
}
```

- 문제
    - 불변식을 깨뜨릴 의도로 임의 생서한 바이트 스트림을 건네면 정상적인 생성자로 만들수 없는 객체를 생성해낼 수 있다.
- 해결
    - readObject 메서드가 defaultReadObject를 호출한 다음 역직렬화된 객체가 유효한지 검사해야 한다.
        
        → 이 작업으로 공격자가 허용되지 않는 Period 인스턴스를 생성하는 일을 막을 수 있지만, 정상 Period 인스턴스에서 시작된 바이트 스트림 끝에 private Date 필드로의 참조를 추가하면 가변 Period 인스턴스를 만들  수 있다.
        

```java
public class MutablePeriod {

    //Period 인스턴스
    public final Period period;

    //시작 시각 필드 - 외부에서 접근할 수 없어야 한다.
    public final Date start;
    //종료 시각 필드 - 외부에서 접근할 수 없어야 한다.
    public final Date end;

    public MutablePeriod() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);

            //유효한 Period 인스턴스를 직렬화한다.
            out.writeObject(new Period(new Date(), new Date()));

            /**
             * 악의적인 '이전 객체 참조', 즉 내부 Date 필드로의 참조를 추가한다.
             * 상세 내용은 자바 객체 직렬화 명세의 6.4절을 참고
             */
            byte[] ref = {0x71, 0, 0x7e, 0, 5}; // 참조 #5
            bos.write(ref); // 시작 start 필드 참조 추가
            ref[4] = 4; //참조 #4
            bos.write(ref); // 종료(end) 필드 참조 추가

            // Period 역직렬화 후 Date 참조를 훔친다.
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            period = (Period) in.readObject();
            start = (Date) in.readObject();
            end = (Date) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }
}
```

- 문제
    - 공격자는 ObjectInputStream에서 Period 인스턴스를 읽은 후 스트림 끝에 추가된 이 ‘악의적인 객체 참조’를 읽어 Period 객체 내부  정보를 얻을 수 있다.
        
        → 이 참조로 Date 인스턴스들을 수정할 수 있으니 Period 인스턴스는 더는 불변이 아니게 된다.
        
        → readObject 메서드가 방어적 복사를 충분히 하지 않았기 때문
        
- 해결
    - 객체를 역직렬화할 때는 클라이언트가 소유해서는 안되는 객체 참조를 갖는 필드를 모두 반드시 방어적으로 복사해야한다.
    - start, end 필드에서 final 한정자를 제거해야한다.
    
    ```java
    public final class Period implements Serializable {
    
        private Date start;
        private Date end;
    
        public Period(Date start, Date end) {
            this.start = new Date(start.getTime()); // 방어적 복사
            this.end = new Date(end.getTime());     // 방어적 복사
    
            // 유효성 검사
            if (this.start.compareTo(this.end) > 0)
                throw new IllegalArgumentException(this.start + "가 " + this.end + "보다 늦다.");
        }
    
        public Date start() {
            return new Date(start.getTime());
        }
    
        public Date end() {
            return new Date(end.getTime());
        }
    
        private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
            s.defaultReadObject();
    
            this.start = new Date(start.getTime()); // 방어적 복사
            this.end = new Date(end.getTime());     // 방어적 복사
    
            if (this.start.compareTo(this.end) > 0)
                throw new IllegalArgumentException(this.start + "가 " + this.end + "보다 늦다.");
        }
    }
    ```
    

### 2. 기본 readObject  메서드를 사용 판단 방법

- transient  필드를 제외한 모든 필드의 값을 매개변수로 받아 유효성 검사없이 필드에 대입하는 public 생성자를 추가해도 괜찮은가? 답이 아니오라면 커스텀 readObject 메서드를 만들어 모든 유효성 검사와 방어적 복사를 수행해야 한다.
    
    혹은 프록시 패턴을 사용하는 방법도 있다. 이 패턴은 역직렬화를 안전하게 만드는 데 필요한 노력을 상당히 경감해 준다.
    
- final이 아닌 직렬화 가능 클래스라면 readObject와 생성자의 공통점이 하나 더 있다. 마치 생성자처럼 readObject 메서드도 재정의 가능 메서드를 호출해서는 안된다. 해당 메서드가 재정의 되면, 하위 클래스의 완전 역직렬화되기 전에 하위 클래스에서 재정의된 메서드가 실행되어 프로그램 오작동으로 이어질 것이다.

### 3. 핵심정리

readObject 메서드를 작성할 때는 언제나 public 생성자를 작성하는 자세로 임해야 한다. readObject는 어떤 바이트 스트림이 넘어오더라도 유효한 인스턴스를 만들어내야 한다. 바이트 스트림이 진짜 직렬화된 인스턴스라고 가정해서는 안된다.

- private 이어야 하는 객체 참조 필드는 각 필드가 가리키는 객체를 방어적으로 복사하라. 불변 클래스 내의 가변 요소가 여기 속한다.
- 모든 불변식을 검사하여 어긋나는게 발견되면 InvalidObjectExceptiojn을 던진다. 방어적 복사 다음에는 반드시 불변식 검사가 뒤따라야 한다.
- 역직렬화 후 객체그래프 전체의 유효성을 검사해야 한다면 ObjectInputValidation 인터페이스를 사용하라.
- 직접적이든 간접적이드, 재정의할 수 있는 메서드는 호출하지 말자.