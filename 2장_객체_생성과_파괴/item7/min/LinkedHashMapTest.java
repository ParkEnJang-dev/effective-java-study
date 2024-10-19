package chapter2.item7.min;

import java.util.LinkedHashMap;

public class LinkedHashMapTest {

    public static void main(String[] args) {
        LinkedHashMap<Integer, String> map
                = new MyLinkedHashMap<>(16, .75f, true);
        map.put(1, null);
        map.put(2, null);
        map.put(3, null);
        map.put(4, null);
        map.put(5, null);
        map.put(6, null);

        map.entrySet().stream().forEach(el -> System.out.println(el));
    }
}
