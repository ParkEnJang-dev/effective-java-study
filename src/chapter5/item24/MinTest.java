package chapter5.item24;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class MinTest {

    public static void main(String[] args) {

//        List<String> strings = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Hello");
        arrayList.add("Hello2");
        List<?> list = arrayList;


        unsafeAdd(arrayList);


        for (Object o : list) {
            System.out.println(o);
        }
//        List<?> strings = new ArrayList<>();
//
//        strings.add("Hello");
//        strings.add("Hello");
//        strings.add(33);

        //런타임 에러 발생
        //String string = (String) strings.get(1);

//        unsafeAdd(strings, Integer.valueOf(42));
//        String s = strings.get(0); // Has compiler-generated cast
    }

    private static void unsafeAdd(List<?> list) {
        list.add(null);
//        list.add("ff");
        for (Object o : list) {
            System.out.println(o);
        }
    }
}
