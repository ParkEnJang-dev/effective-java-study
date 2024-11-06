# int 상수 대신 열거 타입을 사용하라

### 상수 패턴
```java
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int APPLE_GRANNY_SMITH = 2;

public static final int ORANGE_NAVEL = 0;
public static final int ORANGE_TEMPLE = 1;
public static final int ORANGE_BLOOD = 2;
```

- 타입 안전을 보장할 방법도 없고, 표현력도 좋지 않다.
- 오렌지를 보낼 메서드에 사과를 보내고 == 연산자로 비교해도 정상적인 결과가 나올 것이다.
- 어쩔수 없이 접두어를 써서 이름 충돌을 방지.
- 정수 상수는 문자열로 출력하기 까다롭다.

### 열거 타입
```java
public enum Apple { FUJI, PIPPIN, GRANNY_SMITH }
public enum Orange { NAVEL, TEMPLE, BLOOD }
```

- 열거 타입 자체는 클래스, 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개한다.
- 싱글턴을 일반화한 형태
- 컴파일타임 타입 안정성 제공.
  - Apple 매개변수로 받는 메서드를 선언하면, 3가지 값 중 하나임이 확실. 다른값을 넘기려면 컴파일 오류 난다.
- 네임 스페이스를 제공, 이름이 같은 상수도 공존할 수 있다.
  - `Apple.ONE` , `Orange.ONE` 은 구분된다.
- `toString()` 메서드는 적합한 문자열은 내어준다.
- 열거타입에서는 다양한 메서드나 필드도 추가 가능하다.

### Plant 데이터 예
```java
enum Planet {
    MERCURY(3.302e+23, 2.439e6),
    VENUS(4.869e+24, 6.052e6),
    EARTH(5.975e+24, 6.378e6),
    MARS(6.419e+23, 3.393e6),
    JUPITER(1.899e+27, 7.149e7),
    SATURN(5.685e+26, 6.027e7),
    URANUS(8.683e+25, 2.556e7),
    NEPTUNE(1.024e+26, 2.477e7);

    private final double mass; // 질량 (단위: 킬로그램)
    private final double radius; // 반지름 (단위: 미터)
    private final double surfaceGravity; // 표면중력 (단위: m / s^2)

    // 중력상수 (단위: m^3 / kg s^2)
    private static final double G = 6.677300E-11;

    // 생성자
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        this.surfaceGravity = G * mass / (radius * radius);
    }

    public double mass() { return mass; }
    public double radius() { return radius; }
    public double surfaceGravity() { return surfaceGravity; }

    public double surfaceWeight(double mass) {
        return mass * surfaceGravity; // F = ma
    }
}

```
- 클래스 내부에서 특정 필드에 값을 할당 하고 싶다면, 생성자를 통해 할당하면 된다.
  - 모든 필드는 `public static final` 로 공개함

```java
    double earthWeight = Double.parseDouble("1");
    double mass = earthWeight / Planet.EARTH.surfaceGravity;

    for (Planet value : Planet.values()) {
        System.out.printf("%s에서의 무게는 %f이다.%n", value, value.surfaceWeight(mass));
    }
    
```
- enum 클래스는 values() 메서드 제공, 모든값들이 배열의 형태로 제공 된다.
- 행성이 하나 줄어도 출력하는 줄 수가 하나 줄어들 뿐 그 이상의 큰 변화는 없다.

### 상수에깂에 따라 분기하는 열거 타입
```java
enum Operation {
    PLUS, MINUS, TIMES, DIVIDE;

    public double apply(double x, double y) {
        switch (this) {
            case PLUS -> {
                return x+y;
            }
            case MINUS -> {
                return x-y;
            }
            case TIMES -> {
                return x*y;
            }
            case DIVIDE -> {
                return x/y;
            }
        }
        throw new AssertionError("알 수 없는 연산: " + this);
    }
}

```
- 단점
  - 새로운 상수를 추가하면 case문도 추가 해야한다.
  - 실수 하게 되면 알 수 없는 연산이라고 에러 난다.

```java
enum Operation {
    PLUS {
        @Override
        public double apply(double x, double y) {
            return x+y;
        }
    },
    MINUS {
        @Override
        public double apply(double x, double y) {
            return x-y;
        }
    },
    TIMES {
        @Override
        public double apply(double x, double y) {
            return x*y;
        }
    },
    DIVIDE {
        @Override
        public double apply(double x, double y) {
            return x/y;
        }
    };

    public abstract double apply(double x, double y);
}

```
- 값에 무언가를 빼먹을 일이 없다

### 상수별 클래스 몸체와 데이터를 사용한 열거 타입
```java
enum Operation {
    PLUS ("+") {
        @Override
        public double apply(double x, double y) {
            return x+y;
        }
    },
    MINUS ("-") {
        @Override
        public double apply(double x, double y) {
            return x-y;
        }
    },
    TIMES ("*") {
        @Override
        public double apply(double x, double y) {
            return x*y;
        }
    },
    DIVIDE ("/") {
        @Override
        public double apply(double x, double y) {
            return x/y;
        }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public abstract double apply(double x, double y);

    @Override
    public String toString() {
        return symbol;
    }
}



public static void main(String[] args) {
    double x = 10;
    double y = 15;
    //계산식 출력이 편하다.
    for (Operation value : Operation.values()) {
        System.out.printf("%f %s %f = %f%n", x, value, y, value.apply(x, y));
    }
}

```
- symbol 필드를 두어 +,-,*,/ 등 알맞은 기호를 저장했다.

### 열거 타입용 fromString 메서드 구현하기
```java
public static final Map<String, Operation> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Object::toString, e -> e));

    public static Optional<Operation> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }

```
- `fromString` 은 지정한 문자열에 해당하는 Operation을 (존재한다면) 반환한다.
- Optional 반환도 주의 이는 연산이 존재하지 않을 수 있을을 알리는 것이다.


### 값에 따라 분기하여 코드를 공유하는 열거 타입 - 좋은 방법인가?
```java
enum PayrollDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    private static final int MINS_PER_SHIFT = 8 * 60;

    int pay(int minutesWorked, int payRate) {
        int basePay = minutesWorked * payRate;
        int overtimePay;

        switch(this) {
            // 주말
            case SATURDAY : case SUNDAY :
                overtimePay = basePay / 2;
                break;
            // 주중
            default:
                overtimePay = minutesWorked <= MINS_PER_SHIFT ? 0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
        }

        return basePay + overtimePay;
    }
}

```

- 관리관점에 위험한 코드이다. 새로운 값을 추가하려면 case 에도 잊지 말고 넣어줘야 한다.
- 해결책
  1. 잔업수당을 계산하는 코드를 모든 상수에 중복해서 넣으면 된다.
  2. 계산 코드를 평일용과 주말용으로 나눈다.

### 전략 열거 타입 패턴
- 상수를 추가할 때 잔업수당 '전략'을 선택하도록 한다.
- switch 문보다 복잡하지만 더 안전하고 유연하다.
```java
enum PayrollDay {
    MONDAY(PayType.WEEKDAY)
    , TUESDAY(PayType.WEEKDAY)
    , WEDNESDAY(PayType.WEEKDAY)
    , THURSDAY(PayType.WEEKDAY)
    , FRIDAY(PayType.WEEKDAY)
    , SATURDAY(PayType.WEEKEND)
    , SUNDAY(PayType.WEEKEND);

    private final PayType payType;

    PayrollDay(PayType payType) {
        this.payType = payType;
    }

    public int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    // 전략 열거 타입
    enum PayType {
        WEEKDAY {
            @Override
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINS_PER_SHIFT ? 0 : (minsWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            @Override
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        };

        abstract int overtimePay(int mins, int payRate);
        private static final int MINS_PER_SHIFT = 8 * 60;

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }
}
```

### 정리
- 필요한 원소를 컴파일타임에 다 알 수 있는 상수 집합이라면 열거 타입을 사용하자.
  - 태양계 행성, 한 주의 요일, 체스 말
  - 메뉴 아이템, 연산 코드, 명령줄 플래그
- 정수 상수보다 뛰어나다.
