package chapter3.item14.min;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Item14_Main {
    public static void main(String[] args) {
        String[] arr = {"b", "c", "a"};

        //조금만 구현해도 강력한 기능을 제공한다.
        Set<String> s = new TreeSet<>();
        Collections.addAll(s, arr);
        System.out.println(s);

        Integer i = 1;
        System.out.println(i.compareTo(0));


        BigDecimal bd = BigDecimal.valueOf(1.0);
        BigDecimal bd2 = BigDecimal.valueOf(2.0);
        BigDecimal sum = bd2.abs();

    }
}
