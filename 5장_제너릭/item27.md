# item27. 비검사 경고를 제거하라

```java
Set<Lark> exaltation = new HashSet();
```
- HashSet 에 `<>` 추가 경고 발생. `<>`연산자는 자바 7부터 가능
- 할 수 있는 한 모든 비검사 경고를 제거하라.
- 타입 안정성이 보장 된다면 @SuppressWarnings("unchecked") 적용 하여 경고를 숨기자.

- @SuppressWarnings 은 클래스 전체 부터 개별 지역변수 까지 선언 가능하지만, 항상 좁은 범위에 적용하자.
  클래스 전체 적용 해서는 안된다.

```java

    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            //비검사 경고
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

```
- @SuppressWarnings 를 추가해 법위를 최소한 으로 좁힌다.
- 항상 주석도 남겨야 한다.
- 
```java

    public <T> T[] toArray(T[] a) {
        if (a.length < size){
            // 생성한 배열과 매개변수로 받은 배열의 타입이 모두 T[]로 같으므로 올바른 형변환.
            @SuppressWarnings("unchecked") T[] result = (T[]) Arrays.copyOf(elementData, size, a.getClass());
            return result;
        }
            
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

```

### 정리
- 보통 제네릭 타입을 지정해서 비검사 경고를 제거 가능하다.
- 하지만 아래의 경우에는 비검사 경고가 발생하므로, @SuppressWarnings("unchecked") 해줘야 한다.
  아니면, List<String> 해줘서 안정성과 명확성을 확보한다.
- 신중하게 작성 해야 한다. 꼭 주석 필요.
```java
    
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        addUnchecked(strings, "Hello");
    }

    // 제네릭 경고 억제
    @SuppressWarnings("unchecked")
    private static void addUnchecked(List list, String element) {
        // 비검사 경고 발생
        list.add(element);
    }

```
