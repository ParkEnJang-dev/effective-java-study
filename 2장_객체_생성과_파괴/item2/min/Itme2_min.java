package chapter2.item2.min;

public class Itme2_min {
    public static void main(String[] args) {
        //Lombok을 사용하지 않고 Builder 패턴을 사용한 예시
        //객체 생성시 매개변수 확인이 쉽다.
        //매개변수가 많을때 사용하기 좋다.
        NutritionFacts cocacola = new NutritionFacts
                .Builder(240, 8)
                .calories(100).fat(35).build();
    }
}
