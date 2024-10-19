package chapter4.item18.min;

public class AnimalV2 implements AnimalFeature {
    public void makeSound() {
        System.out.println("Animal is making a sound.");
    }

    public String animalType() {
        System.out.println("동물");
        return "동물";
    }
}
