package chapter3.item10.min;

import java.util.Objects;

public class CaseInsensitiveString {

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // 대칭성 위배!
    // CaseInsensitiveString의 equals는 일반 String을 알고 있지만
    // String의 equals는 CaseInsensitiveString을 모른다.

//    @Override
//    public boolean equals(Object o) {
//        if (o instanceof CaseInsensitiveString)
//            return s.equalsIgnoreCase(
//                    ((CaseInsensitiveString) o).s);
//        if (o instanceof String) // 한 방향으로만 작동한다!
//            return s.equalsIgnoreCase((String) o);
//        return false;
//    }

    //문제해결 대칭성.
    @Override public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString &&
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }
}
