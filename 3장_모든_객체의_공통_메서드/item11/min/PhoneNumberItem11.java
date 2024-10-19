package chapter3.item11.min;

public final class PhoneNumberItem11 {
    private final short areaCode, prefix, lineNum;

    public PhoneNumberItem11(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "지역코드");
        this.prefix = rangeCheck(prefix, 999, "프리픽스");
        this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val;
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumberItem11))
            return false;
        //다 같으면 내부 변수를 검사.
        PhoneNumberItem11 pn = (PhoneNumberItem11)o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn. areaCode == areaCode;
    }

//    @Override
//    public int hashCode() {
//        int result = Integer.hashCode(areaCode);
//        result = 31 * result + Integer.hashCode(prefix);
//        result = 31 * result + Integer.hashCode(lineNum);
//        return result;
//    }


    //이렇게 하면 동시성 관리도 해줘야 된다.
    private int hashCode;

    @Override
    public int hashCode() {
        int result = hashCode; // 초기값 0을 가진다.
        if(result == 0) {
            result = Integer.hashCode(areaCode);
            result = 31 * result + Integer.hashCode(areaCode);
            result = 31 * result + Integer.hashCode(areaCode);
            hashCode = result;
        }
        return result;
    }
}
