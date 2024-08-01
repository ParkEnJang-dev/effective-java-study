package chapter4.item21;

import java.util.Collection;
import java.util.Collections;

public class MainTest implements Animal{


    public static void main(String[] args) {
        MainTest mainTest = new MainTest();
        mainTest.animalTypePrint();
        Collections.synchronizedCollection(Collections.emptyList());


    }

    public void makeSoundV2() {
        animalTypePrint();
    }

    @Override
    public void makeSound() {

    }

    @Override
    public String animalType() {
        return null;
    }
}
