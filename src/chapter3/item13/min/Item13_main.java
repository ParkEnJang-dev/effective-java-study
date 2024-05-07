package chapter3.item13.min;

import java.util.Hashtable;

public class Item13_main {


    public static void main(String[] args) {
        PhoneNumber p = new PhoneNumber();

        PhoneNumber p2 = p;
        /*try {
            PhoneNumber clone = p.clone();
            System.out.println(p);
            System.out.println(p2);
            System.out.println(clone);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }*/

        p.clone();

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.clone();


        Stack stack = new Stack();
        stack.push(1);
        stack.push(2);

        //제대로 동작하기위해 elements를 clone해야한다.
        Stack cloneStack = stack.clone();
        cloneStack.push(3);
    }
}
