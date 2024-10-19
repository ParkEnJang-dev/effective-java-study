package chapter2.item7.min;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.WeakHashMap;

public class WeakHashMapTest {

    public static void main(String[] args) {
        WeakHashMap<Integer, String> weakHashMap = new WeakHashMap<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();
        Integer key1 = 1000;
        Integer key2 = 2000;
        Integer key3 = 1000;
        Integer key4 = 2000;

        hashMap.put(key1, "hash test a");
        hashMap.put(key2, "hash test b");
        weakHashMap.put(key3, "test a");
        weakHashMap.put(key4, "test b");
        //linkedHashMap.put(key3, "linked test a");
        //linkedHashMap.put(key4, "linked test b");

        key3 = null;
        key1 = null;
        System.gc();  //강제 Garbage Collection

        weakHashMap.entrySet().stream().forEach(el -> System.out.println(el));
        hashMap.entrySet().stream().forEach(el -> System.out.println(el));
        //linkedHashMap.entrySet().stream().forEach(el -> System.out.println(el));


    }
}
