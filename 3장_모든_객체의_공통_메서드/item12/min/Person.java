package chapter3.item12.min;

public class Person {

    private String name;

    Person(String name) {
        this.name = name;
        System.out.println("휴먼 입니까 ?");
    }

    @Override
    public String toString() {

        return String.format("my name is %s, ", name);
    }
}
