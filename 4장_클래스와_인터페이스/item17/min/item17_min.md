# item 17. 변경 가능성을 최소화하라



### 정리
* 불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이자.
* 생성자와 정적 팩터리 외에는 그 어떤 초기화 메서드도 public 으로 제공해서는 안 된다.


### 불변객체 만드는 방법
```java

public class Complex {
	private final double re;
	private final double im;

	private Complex(double re, double im) {

		this.re = re;this.im = im;
	}

	public static Complex valueOf(doulble re, double im) {
		return new Complex(re, im);
	}
}
```

합당한 이유가 없다면 모든 필드느 private final 로 선언하자.