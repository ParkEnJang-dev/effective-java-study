# item46) 스트림에는 부작용 없는 함수를 사용하라
- 순수함수를 사용해야한다.

### 빈도표 초기화에 스트림을 적절하지 못하게 혹은 적절하게 사용하는 예
- 단순 반복 코딩
```java
Map<String, Long> freq = new HashMap<>();
        try (Stream<String> words = new Scanner(file).tokens()) {
        words.forEach(word -> {
        freq.merge(word.toLowerCase(), 1L, Long::sum);
        });
        }
```

### 스트림을 제대로 활용해 빈도표를 초기화한다.
```java

        Map<String, Long> freq;
        try (Stream<String> words = new Scanner(file).tokens()) {
            freq = words
                    .collect(groupingBy(String::toLowerCase, counting()));
        }
```

- foreach은 스트림 계산 결과 보고 말고는 계산용 으로 사용하지 말자.

### 빈도표에서 가장 흔한 단어 10개를 뽑아내는 파이프 라인
```java
List<String> topTen = freq.keySet().stream()
                .sorted(comparing(freq::get).reversed())
                .limit(10)
                .collect(toList());
```
- Collectors의 멤버를 정적 임포트하여 쓰면 가독성이 좋아진다.


### toMap 수집기를 사용하여 문자열을 열거 타입 상수에 매핑한다.
```java
private static final Map<String, Operation> stringToEnum =
    Stream.of(values()).collect(
        toMap(Object::toString, e -> e));
```
- 스트림 각 원소가 고유한 키에 매핑 되어 있을때 적합하다. 다수가 같은키를 사용하고 있으 IllegalStateException을 던지며 종료 될 것이다.

### 각 키와 해당 키의 특정 원소를 연관 짓는 맵을 생성하는 수집기
```java
Map<Artist, Album> topHits = albums.stream().collect(
        toMap(Album::artist,
                album -> album,
                maxBy(comparing(Album::sales))
        )
);
```

- 음악가의 앨범들을 담은 스트림을 가지고, 음악가와 그 음악가의 베스트 앨범을 연관 시키는 코드입니다. 여기서 비교자로는 BinaryOperator에서 정적 임포트한 maxBy라는 정적 팩토리 메서드를 사용했습니다. maxBy는 Comparator<T>를 입력받아 BinaryOperator<T>를 반환합니다.

### 마지막에 쓴 값을 취하는 수집기
```java
toMap(keyMapper, valueMapper, (oldVal, newVal) -> newVal)
```

-  toMap()은 마지막 인수로 맵 팩토리를 받습니다. 이 인수로는 EnumMap이나 TreeMap처럼 원하는 특정 맵 구현체를 직접 지정할 수 있습니다.

### Collectors의 groupingBy()
- groupingBy는 입력으로 분류 함수(classifier)를 받고 출력으로는 원소들을 카테고리별로 모아 놓은 맵을 담은 수집기를 반환합니다. 반류 함수는 입력받은 원소가 속하는 카테고리를 반환합니다. 그리고 이 카테고리가 해당 원소의 맵 키로 쓰입니다.
  groupingBy가 반환하는 수집기가 리스트 외의 값을 갖는 맵을 생성하게 하려면, 분류 함수와 함께 다운스트림(downstream) 수집기도 명시해야 합니다. 다운 스트림 수집기의 역할은 해당 카테고리의 모든 원소를 담은 스트림으로부터 값을 생성는 것입니다.
```java
    strList.stream().collect(groupingBy(word -> alphabetize(word)))

    //...

    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
```

### toSet()
- toSet() 넘기기
```java
Map<String, Set<String>> collect = stringList.stream()
                .collect(groupingBy(String::toLowerCase, toSet()));
```

### 정리
부작용 없는 함수 객체 사용에 있다. forEach는 결과를 보고할 때만 이용해야 한다.
가장 중요한 수집기 팩터리는 toList, toSet, toMap, groupingBy, joining이다. 