# item47) 반환 타입으로는 스트림보다 컬렉션이 낫다

### Java8 이후 시퀀스 반환 방법
- 기존에는 Collection, Iterable 이라는 선택지가 존재했다.
- Java8 이후로는 Stream 이라는 선택지가 추가되었다.


### 자바 타입 추론의 한계로 컴파일되지 않는다.
```java
//끔직한 우회 방법 이다.
//수동 케이스 필요.
Iterable<ProcessHandle> phs = ProcessHandle.allProcesses()::iterator;

for (ProcessHandle ph : phs) {
    System.out.println("processHandle = " + processHandle.info());
}

// 컴파일 에러
//for (ProcessHandle ph : ProcessHandle.allProcesses()::iterator) {
//    System.out.println("processHandle = " + processHandle.info());
//}
```

### Stream<E>를 Iterable<E>로 중개해주는 어댑터
```java
public static <E> Iterable<E> iterableOf(Stream<E> stream) {
    return stream::iterator;
}

for (ProcessHandle ph : iterableOf(ProcessHandle.allProcesses())) {
    //프로세스를 처리한다.
}

```

### Iterable<E>를 Stream<E>로 중개해주는 어댑터
```java
public static <E> Stream<E> streamOf(Iterable<E> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false);
}
```

- Collection 인터페이스는 stream() 과 Iterable 구현 모두 하기 때문에 기왕이면 Collection 이나 그 하위 타입을 반환 혹은 파라미터 타입에 사용하는 게 최선이다.
- 단지 컬렉션을 반환한다는 이유로 덩치 큰 시퀀스를 메모리에 올려서는 안된다.
  - 시퀀스가 크지만, 표현 방식이 간단해질 수 있다면, 전용 컬렉션을 구현해보자.

### 입력 집합의 멱집합을 전용 컬렉션에 담아 반환한다.
```java
static class PowerSet {
    public static final <E> Collection<Set<E>> of(Set<E> s) {
        List<E> src = new ArrayList<>(s);
        // 30으로 제한하는 이유는 Integer.MAX_VALUE의 범위가 2^31 - 1이기 때문이다.
        int numberOfMaximumElements = 30;

        if(src.size() > numberOfMaximumElements){
            throw new IllegalArgumentException("집합에 원소가 너무 많습니다. (최대 " + numberOfMaximumElements + " 개)");
        }

        return new AbstractList<>() {
            
            @Override
            public int size() {
                // 멱집합의 크기는 2를 원래 집합 원소 수만큼 거듭제곱한 것과 같다.
                return 1 << src.size();
            }

            @Override
            public boolean contains(Object o) {
                return o instanceof Set && src.containsAll((Set) o);
            }

            @Override
            public Set<E> get(int index) {
                Set<E> result = new HashSet<>();

                for (int i = 0; index != 0; i++, index >>= 1) {
                    if ((index & 1) == 1) {
                        result.add(src.get(i));
                    }
                }

                return result;
            }
        };
    }
}
```

- 멱집합을 구해야 하는 경우엔 굳이 항상 모든 컬렉션 요소를 메모리상에 올리고 있을 필요는 없다.
  - get() 메서드를 통해 필요한 시점에 필요한 엘리먼트를 얻으면 된다.
  - 모든 요소를 가지고 있기엔 2^length만큼의 공간을 확보해야 하는 부담이 있다.
  - 모든 요소를 가지고 있으면 매번 변경사항이 생길 때마다 멱집합을 새로 구해야 한다.

### 입력 리스트의 모든 부분리스트를 스트림으로 반환한다.
```java
static class SubLists {
    public static <E> Stream<List<E>> of(List<E> list) {
        Stream<List<E>> prefixes = prefixes(list);
        System.out.println("prefixes = " + prefixes.toList());

        Stream<List<E>> suffixes = suffixes(list);
        System.out.println("suffixes = " + suffixes.toList());

        return Stream.concat(prefixes(list), suffixes(list));
    }

    private static <E> Stream<List<E>> prefixes(List<E> list) {
        return IntStream.rangeClosed(1, list.size())
                .mapToObj(end -> list.subList(0, end));
    }

    private static <E> Stream<List<E>> suffixes(List<E> list) {
        return IntStream.range(0, list.size())
                .mapToObj(start -> list.subList(start, list.size()));
    }
}

```



### 정리