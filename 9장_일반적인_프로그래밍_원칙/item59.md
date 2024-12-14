# item59) 라이브러리를 익히고 사용하라

### 흔하지만 문제가 심각한 코드!
```java
static Random rnd = new Random();

static int random(int n) {
    return Math.abs(rnd.nextInt()) % n;
}

```
- 3가지 문제
  - 2의 제곱수라면 얼마 지나지 않아 같은 수열이 반복.
  - 2의 제곱수가 아니라면 몇몇 숫자가 평균적으로 더 자주 반환된다.n값이 크면 위 현상이 두드러진다.
  - 지정한 범위 바깥 수가 종종 튀어 나 올 수있다. 
    - Random.nextInt(int)를 사용하면 이 문제를 해결할 수 있다.

```java
public static void main(String[] args){
    int n = 2 * (Integer.MAX_VALUE / 3);
    int low = 0;
    for (int i = 0; i < 100000; i++){
        if (random(n) < n/2){
            low++;
        }
    }
    System.out.println(low);
}
```

- random 메서드가 이상적으로 동작한다면 약 50만 개가 출력돼야 하지만, 실제로 실행하면 666,666에 가까운 값을 얻는다. 2/3 가량이 중간값보다 낮은 쪽으로 쏠렸다.

### transferTo 메서드를 이용해 URL의 내용 가져오기 - 자바 9부터
```java
public static void main(String[] args) throws IOException {
  try (InputStream in = new URL("http://www.google.com").openStream()) {
    in.transferTo(System.out);
  }
}
```

- 자바프로그래머 라면 java.lang, java.util, java.io 하위 패키들에는 익숙해져야 한다.
- 컬렉션 프래임워크, 스트림 라이브러리, java.util.concurrent의 동시성 기능도 마찬가지다.

### 핵심 정리
- 바퀴를 다시 발명하지 말자. 검증된 라이브러리를 사용하면 된다.