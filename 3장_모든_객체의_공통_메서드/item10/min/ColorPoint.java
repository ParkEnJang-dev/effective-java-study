package chapter3.item10.min;

import java.awt.*;
import java.util.Objects;

public class ColorPoint extends Point {
    private final Point point;
    private final Color color;

    public ColorPoint (int x, int y, Color color) {
        point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }

//    public ColorPoint(int x, int y, Color color) {
//        super(x, y);
//        this.color = color;
//    }

//    @Override public boolean equals(Object o) {
//        if (!(o instanceof ColorPoint))
//            return false;
//        return super.equals(o) && ((ColorPoint) o).color == color;
//    }

    //색깔 정보를 무시하고 비교하면 대칭성은 지키지만, 추이성을 깬다.
//    @Override public boolean equals(Object o) {
//        if (!(o instanceof Point))
//            return false;
//
//        // o가 일반 Point면 색상을 무시하고 비교한다.
//        if (!(o instanceof ColorPoint))
//            return o.equals(this);
//
//        // o가 ColorPoint면 색상까지 비교한다.
//        return super.equals(o) && ((ColorPoint) o).color == color;
//    }

    public Point asPoint() {
        return point;
    }

    @Override public boolean equals(Object o) {
//        if (!(o instanceof MyType))
//            return false;
//        MyType mt = (MyType) o;

        if (!(o instanceof ColorPoint))
            return false;
        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}
