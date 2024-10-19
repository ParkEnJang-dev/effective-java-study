package chapter4.item21;

public interface Animal {

    void makeSound();
    String animalType();
    default void animalTypePrint() {
        System.out.println("동물");
    }
}
