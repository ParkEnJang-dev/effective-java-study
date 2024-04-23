package chapter2.item3.min;

import java.util.ArrayList;
import java.util.List;

public class Item3_main {

    public static void main(String[] args) {
        // 외부에서 변경가능
        Elvis.instance = null;

        // Thread safe 하지 않음. (상위 주석 후 수차례 반복 시 확인 가능)
        Runnable runnable = () -> {
            Elvis singleton = Elvis.getInstance();
            System.out.println( singleton );
        };

        List<Thread> threads = new ArrayList<>();
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );
        threads.add( new Thread( runnable ) );

        for ( int i = 0; i < threads.size(); i++ ) {
            Thread thread = threads.get( i );
            thread.start();
        }
    }
}
