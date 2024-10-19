package chapter4.item15.min;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Item15_Main {

    public static final int[] VALUES = {1, 2, 3, 4};

    private static final Integer[] PRIVATE_VALUES = {1, 2, 3, 4};
    public static final List<Integer> VALUES_LIST = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

    public static final Integer[] values(){
        return PRIVATE_VALUES.clone();
    }

    public static void main(String[] args) {

        VALUES[0] = 10;
        //보안의 헛점
        //System.out.println(VALUES[0]);

        //setter 안됨.
        //해결방법 1
        System.out.println(VALUES_LIST);

        //해결방법 2
        //원래 객체는 변하지 않는다.
        Integer[] values = values();
        values[0] = 10;
        System.out.println(values[0]);
        System.out.println(values()[0]);

    }
}
