# Item45. 스트림은 주의해서 사용하라

### 핵심 2가지 
1. 스트림은 데이터 원소의 유한, 무한 시퀀스를 뜻한다.
2. 스트림 파이프라인은 원소들로 수행하는 연산 단계를 표현하는 개념이다.

- 스트림 안의 데이터 원소들은 객체 참조나 기본 타입값이다. 기본타입 으로는 int, long, double을 지원한다.


### 스트림 흐름
- 스트림으로 시작해 종단연산자(terminal-operation) 으로 끝나면 중간에 중간연산자(intermediate-operation)가 있을 수 있다.
- 종단 연산은 중간 연산이 내놓은 스트림에 최후의 연산을 가한다.
- 중간 연산자 - `filter(), map(), sorted()`
- 종단 연산자 - `forEach(), collect(), match(), count(), reduce()`

### 지연 평가(lazy evaluation)
  - 종단 연산이 수행될 때 이뤄지며, 종단 연산에 쓰이지 않는 원소는 계산에 사용되지 않는다. 
  - 무한 스트림을 다룰수 있는 열쇠.
  - 종단 연산이 없으면 아무일도 하지 않는 no-op와 같다.
  - 결과값이 필요할때까지 계산을 늦추는 기법
> 실제로 필요하지 않은 데이터들을 탐색하는 것을 방지해 속도를 높일 수 있다. 즉, 종단 연산에 쓰이지 않는 데이터 원소는 계산 자체에 쓰이지 않는다. 그리고 이것을 Short-Circuit 방식이라 부른다.

### 스트림은 주의해서 사용해야 한다.
- 파이브라인 여러개를 연결해 단 하나의 표현식으로 만들 수 있다. 스트림을 잘못 사용하면 읽기도 어렵고 유지보수가 힘들어 진다.

### 사전 하나를 훓어 원소 수가 많은 아나그램 그룹들을 출력한다.
- 이 프로그램은 사전 파일에서 사용자가 지정한 값보다 원소 수가 많은 아나그램 그룹을 출력한다. 사전 파일에서 단어를 읽어 맵에 저장한다. 키는 그 단어를 구성하는 철자들을 알파벳순으로 정렬한 값이다.
- "staple"의 키는 "aelpst" 되고  "petals" 키도 "aelpst" 로 같은 키를 사용한다.
```java
public class Anagrams {
   public static void main(String[] args) throws IOException {
       File dictionary = new File(args[0]);
       int minGroupSize = Integer.parseInt(args[1]);
       
       Map<String, Set<String>> groups = new HashMap<>();
       try (Scanner s = new Scanner(dictionary)) {
          while (s.hasNext()) {
             String word = s.next();
             
             groups.computeIfAbsent(alphabetize(word),
                ( unused) -> new TreeSet<>()).add(word);
          }
       }
          
       for (Set<String group : groups.values())
          if (group.size() >= minGroupSize)
             System.out.println(group.size() + ": " + group)
    }
       
    private static String alphabetize(String s) {
       char [] a = s.toCharArray();
       Arrays.sort(a);
       return new String(a);
    }
}


```
### 스트림을 과하게 사용한 예시(사용하지 말것)
```java
public class Anagrams {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary)) {
            words.collect(
                            groupingBy(word -> word.chars().sorted()
                                    .collect(StringBuilder::new,
                                            (sb, c) -> sb.append((char) c),
                                            StringBuilder::append).toString()))
                    .values().stream()
                    .filter(group -> group.size() >= minGroupSize)
                    .map(group -> group.size() + ": " + group)
                    .forEach(System.out::println);
        }
    }
}

```

### 스트림을 적절히 활용하면 깔끔하고 명료해진다.
- 스트림 변수를 words로 지어 word임을 명확히 했다.

```java
public class Anagrams {
    public static void main(String[] args) throws IOException {
        Path dictionary = Paths.get(args[0]);
        int minGroupSize = Integer.parseInt(args[1]);

        try (Stream<String> words = Files.lines(dictionary)) {
            words.collect(groupingBy(word -> alphabetize(word)))
                    .values().stream()
                    .filter(group -> group.size() >= minGroupSize)
                    .forEach(group -> System.out.println(group.size() + ": " + group));
        }
    }

}

```

### char 스트림
- 자바가 기본타입인 char용 스트림을 지원하지 않는다.
- “Hello world!”.chars()가 반환하는 스트림의 원소는 char가 아닌 int 값이다.
- char 값들을 처리할 때는 스트림을 삼가하는 편이 좋다.
```java
"Hello world!".chars().forEach(System.out.print);

// 예상 출력 : Hello world!
// 실제 출력 : 7210110810811132119111111410810033

// 올바르게 print 메서드를 호출하려면 아래와 같이 명시적 형변환이 필요하다.
"Hello world!".chars().forEach(x -> System.out.print((char) x));
```

### 주의사항
- 모든걸 스트림으로 바꾸고 싶겠지만 반복문과 적절히 조화 롭게 사용하는것이 좋다.
- 새 코드 더 나아보일때만 Stream 으로 리펙토링 하자.

### 함수 객체(람다 메서드 참조) vs 반복코드(코드 블록)
- 함수 객체(람다 메서드 참조)
  - 지역 변수 수정 불가능
- 반복코드(코드 블록)
  - 지역 변수 수정 가능
  - return 문 또는 continue, break 로 건너 뛸수 있다.
  - 명시된 예외 던질수 있다.

- 스트림 사용 적절 기준
  - 원소들의 시퀀스를 일관되게 변환한다.
  - 원소들의 시퀀스를 필터링한다.
  - 원소들의 시퀀스를 하나의 연산을 사용해 결합한다.(더하기, 연결하기, 최솟값 구하기 등)
  - 원소들의 시퀀스를 컬렉션에 모은다.(아마도 공통된 속성을 기준으로 묶는다)
  - 원소들의 시퀀스에서 특정 조건을 만족하는 원소를 찾는다.


### 정리
- 스트림은 강력하지만 만능은 아니다. 적절한 상황에서 사용해야 한다. 
- 스트림을 사용할 때는 코드의 의도를 명확히 하고, 가독성을 해치지 않도록 주의해야 한다.
- 스트림과 반복문은 서로 배타적인 관계가 아니며, 상황에 맞게 선택적으로 사용해야 한다.