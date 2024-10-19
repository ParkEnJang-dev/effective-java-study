package chapter3.item14.min;

import java.util.Comparator;

class MyObject implements Comparable<MyObject> {
    private int a;
    private double b;

    //성능 개선 방법.
    private static final Comparator<MyObject> COMPARATOR =
            Comparator.comparingInt((MyObject myObj) -> myObj.a)
                    .thenComparing(myObj -> myObj.b);

    @Override
    public int compareTo(final MyObject o) {
        return COMPARATOR.compare(this, o);
    }

    /* 정수 오버플로우를 일으키거나,
    소수인 경우 부동소수점 계산 방식에 따른 오류를 발생시킬 수 있기 때문에 사용하는 것은 좋지 않다

    @Override
    public int compareTo(final MyObject o) {
        return a - o.a;
    }*/
}
