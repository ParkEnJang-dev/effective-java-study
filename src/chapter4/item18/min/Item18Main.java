package chapter4.item18.min;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Item18Main {

    public static void main(String[] args) {

        CustomHashSet<String> customHashSet = new CustomHashSet<>(new HashSet<>());
        List<String> test = Arrays.asList("틱","탁탁","펑");
        customHashSet.addAll(test);

        System.out.println(customHashSet.getAddCount());

        Dog dog = new Dog();
        //dog 에서 재정의 하여 원하는 바와 다르게 동작한다.
        dog.makeSound();


        DogV2 dogV2 = new DogV2();
        dogV2.animalType();
    }
}
