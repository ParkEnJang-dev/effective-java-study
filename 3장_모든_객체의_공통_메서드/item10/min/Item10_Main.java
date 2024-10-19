package chapter3.item10.min;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Item10_Main {
    public static void main(String[] args) {

        CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
        String s = "polish";


        System.out.println(cis.equals(s));

        List<CaseInsensitiveString> list = new ArrayList<>();
        list.add(cis);

        System.out.println(list.contains(s));


        Point p = new Point(1, 2);
        ColorPoint cp = new ColorPoint(1,2, Color.BLACK);

        //대칭성 위배
        System.out.println(p.equals(cp));
        System.out.println(cp.equals(p));

        //대칭성은 지키지만 추이성은 깬다.
        ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
        Point p2 = new Point(1, 2);
        ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);
    }
}
