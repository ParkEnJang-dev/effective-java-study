package chapter2.item6.min;

public class Item6_Main {

    private static final String TEST = "test";

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        sum();
        long end = System.currentTimeMillis();
        System.out.println("수행 시간 : " + (end - start) + "ms");
    }

    private static long sum() {
        long sum = 0;
        for(long i=0; i<=Integer.MAX_VALUE; i++) {
            sum += i;
        }
        return sum;
    }
}
