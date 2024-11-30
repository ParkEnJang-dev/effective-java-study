# item53)가변인수는 신중히 사용하라


### 가변인수
```java
void method(Object... args) { }
```

### 인수가 1개 이상이어야 하는 가변인수 메서드
```java
static int min(int... args) {
    if (args.length == 0)
        throw new IllegalArgumentException("인수가 1개 이상 필요합니다.");
    int min = args[0];
    for (int i = 1; i < args.length; i++)
        if (args[i] < min)
            min = args[i];
    return min;
}
```

- 0개의 인수를 넣으면, 런타임시 에러가 발생한다.
- 코드도 지저분한다

```java
// 개선 방법

static int min(int firstArgs, int... remainingArgs) {
    int min = firstArg;
    for (int arg : remainingArgs)
        if (arg < min)
            min = arg;
    return min;
}
```
- 가변인수는 인수 개수가 정해지지 않았을 때 아주 유용.
- 성능에 민감하면 걸림돌이 된다.

- 가변인수를 사용할때는 호출 인수가 몇 개가 넘어가는지에 따라 성능이 크게 달라질 수 있다.
- 가변인수는 배열을 생성하고 초기화하는 과정이 필요하다.

### 정리
- 메서드 정의할때 필수 매개변수는 가변인수 앞에 두자.
- 가변인수를 사용할 때는 성문 문제 고려하지.