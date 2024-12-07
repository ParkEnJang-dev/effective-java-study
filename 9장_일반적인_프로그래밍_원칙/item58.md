# item58) 전통적은 for 문보다는 for-each 문을 사용하라

- for 문을 사용하여 잘못된 변수를 사용했을 때 컴파일러가 잡아주리라는 보장도 없다.
- for-each 문을 사용하면 반복자와 인덱스 변수를 사용하지 않으니 코드가 깔금해지고 오류가 날 일도 없다.

```java
for (Element e : c) {
        ..
}
```

- 콜론(:)은 "안의(in)" 라고 읽으면 된다.
- 컬랙션을 중첩해 순회해야 한다면 for-each 문의 이점이 더욱 커진다.

### 버그찾기
```java

enum Suit { CLUB, DIAMOND, HEART, SPADE }
enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING} ...
static Collection<Suit> suits = Arrays.asList(Suit.values());
static Collection<Rank> ranks = Arrays.asList(Rank.values()); 
List<Card> deck = new ArrayList<>();

for (Iterator<Suit> i = suites.iterator(); i.hasNext(); )
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
        deck.add(new Card(i.next(), j.next()));

```
- i.next() 부분이 버그. 두 번째 루프에서 i.next()를 호출하면 NoSuchElementException이 발생한다.
</br>
</br>
</br>
- 권장 관용구
```java
for (Suit suit : suits)
    for (Rank rank : ranks)
        deck.add(new Card(suit, rank));
```

### for-each 문을 사용할 수 없는 3가지 경우
- 파과적인 필터링(destructive filtering) 
  - 컬렉션을 순회하면서 선택된 원소를 제거해야 한다면 반복자를 명시적으로 사용해야 한다.
  - 자바 8부터 Collection.removeIf를 사용하면 된다.
- 변형(transforming)
  - 리스트나 배열을 순회하면서 그 원소의 값 일부 혹은 전체를 교체해야 한다면 리스트의 반복자나 배열의 인덱스를 사용해야 한다.
- 병렬 반복(parallel iteration)
  - 여러 컬렉션을 병렬로 순회해야 한다면 각각의 반복자와 인덱스 변수를 사용해야 한다.
  - 예
  ```java
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> ages = Arrays.asList(25, 30, 35);

        // 병렬 반복: 두 리스트를 동기화하며 같은 인덱스를 처리
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + " is " + ages.get(i) + " years old.");
        }
    }
  ```
  
- 원소들의 묶음을 표현하는 타입을 작성해야 한다면 Iterable을 구현하는 쪽으로 고민하라.
```java
public class Elements implements Iterable<String> {
private List<String> items = new ArrayList<>();

    public void add(String item) {
        items.add(item);
    }

    // Iterable 인터페이스의 메서드 구현
    @Override
    public Iterator<String> iterator() {
        return items.iterator();
    }

    public static void main(String[] args) {
        Elements elements = new Elements();
        elements.add("One");
        elements.add("Two");
        elements.add("Three");

        // for-each를 사용하여 Elements 반복
        for (String item : elements) {
            System.out.println(item);
        }
    }
}
```

### 정리
- for-each 문은 명료, 유연하고 버그를 예방해주고, 성능저하도 없다.