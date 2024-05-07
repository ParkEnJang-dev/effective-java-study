package chapter3.item13.min;

public class PhoneNumber implements Cloneable {


    // Cloneable을 안쓰면 에러 난다.
    // Cloneable을 구현하지 않은 클래스의 clone 메서드를 호출하면 CloneNotSupportedException을 던진다.
    @Override
    public PhoneNumber clone()  {
        try {
            return (PhoneNumber) super.clone();
        }catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
