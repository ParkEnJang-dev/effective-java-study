package chapter4.item18.min;

public class DogV2 implements AnimalFeature{

    //의존성 주입 방식
    private AnimalV2 animal;

    public DogV2() {
        this.animal = new AnimalV2();
    }

//    public makeSound() { // makeSound를 재정의했지만 Animal의 기능이 바뀌지 않는다.
//        return "bark";
//    }
//
//    public String eatAfterBark() { // Animal의 기능이 필요하다면 아래와 같이 정의한다.
//        animal.eatFood();
//        return  "bark";
//    }

    @Override
    public void makeSound() {
        System.out.println("Dog is barking.");
    }

    @Override
    public String animalType() {
        return animal.animalType();
    }

}
