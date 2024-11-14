# 명명 패턴보다 애너테이션을 사용하라


### 명명 패턴

* 단점
  1. JUnit3 에서는 테스트 메서드 이름이 test로 시작해야 했다. 오타가 나면 이메서드를 지나치게 된다.
  2. 클래스 이름을 TestSafetyMechanisms로 지어 JUnit에 던져줬다고 해보자. JUnit은 클래스 이름에 관심이 없어 개발자 의도한 테스트는 전혀 수행되지 않는다.
  3. 매개변수로 전달할 방법이 없다. 


Junit4 부터 애너테이션을 전면 도입 하였다.


### 마커(marker) 애너테이션 타입 선언

```java
import java.lang.annotation.*;

/**
 * 테스트 메서드임을 선언하는 애너테이션이다.
 * 매개변수 없는 정적 메서드 전용이다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}

```

- @Retention(RetentionPolicy.RUNTIME) : @Test가 런타임에도 유지.
- @Target(ElementType.METHOD) : @Test가 메서드 선언에만 작동.

### 마커 애너테이션을 사용한 프로그램 예시
- @Test 애너테이션이 Sample 클래스의 의미에 직접적인 영향을 주지는 않는다. 추가 정보를 제공할 뿐이다.
```java

public class Sample {
    @Test
    public static void m1() { }        // 성공해야 한다.
    public static void m2() { }
    @Test public static void m3() {    // 실패해야 한다.
        throw new RuntimeException("실패");
    }
    public static void m4() { }  // 테스트가 아니다.
    @Test public void m5() { }   // 잘못 사용한 예: 정적 메서드가 아니다.
    public static void m6() { }
    @Test public static void m7() {    // 실패해야 한다.
        throw new RuntimeException("실패");
    }
    public static void m8() { }
}
```

### 마커 애너테이션을 처리하는 프로그램
- 이 테스트 러너는 명령줄로부터 완전 정규화된 클래스 이름을 받아, 그 클래스에서 @Test 애너테이션이 달린 메서드를 차례로 호출 한다.
- 애너테이션을 잘못 사용해 예외가 발생하면 오류 메시지를 출력 한다.
```java
import java.lang.reflect.*;

public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName(args[0]);
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                tests++;
                try {
                    m.invoke(null);
                    passed++;
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    System.out.println(m + " 실패: " + exc);
                } catch (Exception exc) {
                    System.out.println("잘못 사용한 @Test: " + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n",
                passed, tests - passed);
    }
}

```

### 매개변수를 받는 애너테이션 타입
- 매개변수 타임은 Throwable을 확장한 클래스의 Class 객체 라는 뜻이며, 따라서 모든 예외(와 오류) 타입을 다 수용한다.
```java
import java.lang.annotation.*;

/**
 * 명시한 예외를 던져야만 성공하는 테스트 메서드용 애너테이션
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Throwable> value();  // 매개변수
}
```

### 매개변수 하나짜리 애너테이션을 사용한 프로그램
```java
public class Sample2 {
    @ExceptionTest(ArithmeticException.class)
    public static void m1() {  // 성공해야 한다.
        int i = 0;
        i = i / i;
    }
    @ExceptionTest(ArithmeticException.class)
    public static void m2() {  // 실패해야 한다. (다른 예외 발생)
        int[] a = new int[0];
        int i = a[1];
    }
    @ExceptionTest(ArithmeticException.class)
    public static void m3() { }  // 실패해야 한다. (예외가 발생하지 않음)
}
```

- 테스트 도구 수정
  - @Test 애너테이션용 코드와 비슷해 보이지만 매개변수의 값을 추출하여 테스트 매서드가 올바른 예외를 던지는지 확인하는데 사용한다.
```java
if (m.isAnnotationPresent(ExceptionTest.class)) {
                tests++;
         try {
               m.invoke(null);
               System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
         } catch (InvocationTargetException wrappedEx) {
               Throwable exc = wrappedEx.getCause();
               Class<? extends Throwable> excType =
               m.getAnnotation(ExceptionTest.class).value();
                if (excType.isInstance(exc)) {
                     passed++;
                } else {
                     System.out.printf("테스트 %s 실패: 기대한 예외 %s, 발생한 예외 %s%n",
                                m, excType.getName(), exc);
                }
           } catch (Exception exc) {
                System.out.println("잘못 사용한 @ExceptionTest: " + m);
           }
}

```

### 배열 매개변수를 받는 애너테이션 타입


```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Exception>[] value();   // Class 객체의 배열
}
```

- 배열 매개변수를 받는 애너테이션을 사용하는 코드
```java
@ExceptionTest({ IndexOutOfBoundsException.class,
                     NullPointerException.class })
public static void doublyBad() {   // 성공해야 한다.
     List<String> list = new ArrayList<>();

     // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나
     // NullPointerException을 던질 수 있다.
     list.addAll(5, null);
}
```
- 배열 매개변수를 받는 애너테이션을 처리하는 코드
```java
if (m.isAnnotationPresent(ExceptionTest.class)) {
        tests++;
        try {
             m.invoke(null);
             System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
         } catch (Throwable wrappedExc) {
             Throwable exc = wrappedExc.getCause();
             int oldPassed = passed;
             Class<? extends Throwable>[] excTypes =
                     m.getAnnotation(ExceptionTest.class).value();
             for (Class<? extends Throwable> excType : excTypes) {
                 if (excType.isInstance(exc)) {
                       passed++;
                       break;
                 }
              }
              if (passed == oldPassed)
                  System.out.printf("테스트 %s 실패: %s %n", m, exc);
              }
          }
 }
```

### 다수의 예외를 명시하는 애너테이션 : @Repeatable
- 여러개의 애너테이션을 받게 할 수 있다.
  1. @Repeatable을 단 애너테이션을 반환하는 '컨테이너 애너테이션'을 하나 더 정의하고, @Repeatable에 이 컨테이너 애너테이션의 class 객체를 매개변수로 전달해야 한다.
  2. 컨테이너 애너테이션은 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야 한다.
  3. 컨테이너 애너테이션 타입에는 적절한 보존 정책(@Retention)과 적용 대상(@Target)을 명시해야 한다.(그렇지 않으면 컴파일 X)

- 반복 가능한 애너테이션 타입
```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)  //컨테이너 애너테이션 class 객체
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}
```

- 반복 가능한 애너테이션의 컨테이너 애너테이션
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
    ExceptionTest[] value();     // value 메서드 정의
}
```

- 반복 가능한 애너테이션을 두 번 단 코드
```java
@ExceptionTest(IndexOutOfBoundsException.class)
@ExceptionTest(NullPointerException.class)
public static void doublyBad() {
      List<String> list = new ArrayList<>();

      // 자바 API 명세에 따르면 다음 메서드는 IndexOutOfBoundsException이나
        // NullPointerException을 던질 수 있다.
      list.addAll(5, null);
 }
```

- 반복 가능 애너테이션 다루기
  - 애너테이션을 여러개 달면 '컨테이너' 애너테이션 타입이 적용되기 때문에 `m.isAnnotationPresent()` 에서 둘을 명확히 구분하고 있는 것을 볼 수 있다.
  - 두개의 애너테이션을 따로따로 확인해야된다.
```java
if (m.isAnnotationPresent(ExceptionTest.class)
                    || m.isAnnotationPresent(ExceptionTestContainer.class)) {
        tests++;
        try {
             m.invoke(null);
             System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
        } catch (Throwable wrappedExc) {
             Throwable exc = wrappedExc.getCause();
             int oldPassed = passed;
             ExceptionTest[] excTests =
                       m.getAnnotationsByType(ExceptionTest.class);
             for (ExceptionTest excTest : excTests) {
                 if (excTest.value().isInstance(exc)) {
                     passed++;
                     break;
                 }
             }
             if (passed == oldPassed)
                 System.out.printf("테스트 %s 실패: %s %n", m, exc);
            }
      }
}
```

### 정리
- 애너테이션으로 할 수 있는 일을 명명 패턴으로 처리할 이유는 없다.
- 자바 프로그래머라면 예외 없이 자바가 제공하는 애너테이션 타입들을 사용해야 한다.(아이템 40, 27)