package chapter2.item8.min;

public class RoomTest {
    public static void main(String[] args) {
        // 여기서는 Room.close()를 호출하지 않는다. 반드시 try-with-resources를 사용한다.
        new Room(99);
        // gc 를 사용하면 Room의 close()가 호출되어 청소가 된다. 그러나 close() 보장 X
        System.gc();
        System.out.println("Room test end.");
    }
}
