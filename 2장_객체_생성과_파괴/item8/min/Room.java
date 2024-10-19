package chapter2.item8.min;

import java.lang.ref.Cleaner;

// 코드 8-1 cleaner를 안전망으로 활용하는 AutoCloseable 클래스
public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    // 청소가 필요한 자원. 절대 Room을 참조해서는 안 된다!
    // 순환참조가 생겨 가비기 컬렉터가 Room 인스턴스를 회수해갈 기회가 오지 않는다.
    private static class State implements Runnable {
        int numJunkPiles; // 방의 쓰래기 수

        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        // close 메서드나 cleaner가 호출한다.
        // 1. Room의 close 메서드 호출 > Cleanable의 clean 호출 > run 호출
        // 2. 가비지 컬렉터가 Room 회수 > cleaner(바라건대) State의 run 호출
        @Override
        public void run() {
            System.out.println("Cleaning room");
            numJunkPiles = 0;
        }
    }

    // 방의 상태. cleanable과 공유한다.
    private final State state;

    // cleanable 객체. 수거 대상이 되면 방을 청소한다.
    private final Cleaner.Cleanable cleanable;

    public Room(int numJunkPiles) {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);

    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
