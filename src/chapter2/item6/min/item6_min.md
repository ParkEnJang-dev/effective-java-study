# item 6. 불피요한 객체 생성을 피하라

* [내용](#내용)
* [성능 개선](#성능 개선)
* [오토박싱](#오토박싱)


### 내용
```java

String s = new String("bikini"); // 따라하지 말 것 - 성능 저하

String s = "bikini"; // 이렇게 쓰자

// String s 에서 계속 붙여서 사용할려면 StringBuilder를 사용하자
StringBuilder sb = new StringBuilder();




```

### 성능 개선
```java
static boolean isRomanNumeralSlow(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
            + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}

// 객체를 재사용해 성능을 개선 한다.
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})"
                    + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

    static boolean isRomanNumeralFast(String s) {
        return ROMAN.matcher(s).matches();
    }
}

```

### 오토박싱



```java
private static long sum() {
	Long sum = 0L;
	for(long i=0; i<=Integer.MAX_VALUE; i++) {
		sum += i;
	}
	return sum;
}
```

* long 타입인 i가 Long 타입인 sum에 더해질 때마다 long을 Long으로 오토박싱한다. 
</br> 이로 인해 성능이 저하된다. sum만 long으로 바꾸면 성능이 개선된다.


* 매서드를 한번도 호출하지 않으면 쓸데없이 초기화 할 필요가 없어 지연초기화를 사용할 수 있다.
</br> 하지만 지연 초기화 불피요한 초기화 없앨 수는 있지만 권하지 않는다.
</br> 코드가 복잡해지고 성능은 크게 개선 되지 않은때가 있다.
</br> 스프링에서는 @Lazy 어노테이션을 사용해서 지연 초기화를 사용할 수 있다.
</br> 하지만 이것도 권하지 않는다.애플리케이션 실행시 즉시초기화는 에러를 바로 발견할수 있지만 지연 초기화는 런타임시 에러가 발생할 수 있다.