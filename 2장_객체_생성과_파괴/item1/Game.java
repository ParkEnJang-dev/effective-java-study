package chapter2.item1;

public class Game {
    private String genre;

    private Game(String genre) {
    }

    //이름을 가질 수 있다.
    //다른 객체 반환 가능
    //생성자에 넘길 매개변수가 많을때.

    //단점
    //생성자는 private 으로 만들어야 되기 때문에 상속이 불가능하다.
    //정적 팩토리 메서드는 다른 정적 메서드와 구분이 안된다. 개발자들이 찾기 어렵다.
    public static Game create(String genre) {
        System.out.println("Game.from");
        return new Game(genre);
    }
}

