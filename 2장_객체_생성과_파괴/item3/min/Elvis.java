package chapter2.item3.min;

public class Elvis {

    public static Elvis instance;
    public static Elvis getInstance(){
        if(instance == null){
            instance = new Elvis();
        }
        return instance;
    }
    /**
     * 장점
     * 구현 간결 싱글턴입을 api 들어냄
     * 단점
     * 싱글턴을 사용하는 클라이언트가 테스트 하기 어려움
     * 역질렬화시 새로운 인스턴스 생길 수 있음.
    public static final Elvis INSTANCE = new Elvis();
    private Elvis(){
    }*/

    /**
    * 제너릭 싱글턴 팩터링 만들 수 있다.
     * 단점 스레드 세이프 하지 않음
    private static final Elvis INSTANCE = new Elvis();
    private Elvis(){}

    public static Elvis getInstance(){
        return INSTANCE;
    }*/

    /* 제너릭한 싱글톤 패턴을 구현 가능

    public class GenericSingleton<T> {
        public static final GenericSingleton<?> INSTANCE = new GenericSingleton<>();

        private GenericSingleton() {
        }

        public static <T> GenericSingleton<T> getInstance() {
            return (GenericSingleton<T>) INSTANCE;
        }

        public boolean send( T message ) {
            // blah~ blah~
            return true;
        }
    }*/

}
