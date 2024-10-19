# item 5. 자원을 직접 명시하지 말고 의존 객체 주입을 사용.


# 내용
1. 책에서 설명하는 spellChecker 예제를 보면, 하나의 객체의 의존해서 사용하는 방식은 잘못된 방식, 유연성이 떨어진다.


생성자를 이용한 의존성 주입
```java

public class SpellChecker3 {
    private final Lexicon dictionary;

    // 생성자를 이용해서 의존성 주입 수행
    public SpellChecker3(Lexicon dictionary) {
        this.dictionary = dictionary;
    }
	...

    public boolean isValid(String word) {
        System.out.println("call is Valid");
        return false;
    }

    public List<String> suggestions(String type){
        System.out.println("call suggestions");
        return null;
    }
	...
}
```
생성자 생성 
        
```java
class KoreaDictionary extends Lexicon{}

Lexicon koreaLexicon = new KoreaDictionary();
SpellChecker3 sc1 = new SpellChecker3(koreaLexicon);
```

- 객체를 생성하고 주입하는 방식은 코드를 어지럽게할 수 도 있다.
- 그래서 프레임 워크를 사용한다. spring, dagger hilts 등등


