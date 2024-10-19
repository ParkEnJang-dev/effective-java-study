package chapter2.item1;

/**
 * Item 1: 생성자 대신 정적 팩터리 메서드를 고려하라
 */
public class Item1_min {



    public static void main(String[] args) {
        Game fps = Game.create("FPS");
    }

}





