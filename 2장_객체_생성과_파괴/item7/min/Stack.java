package chapter2.item7.min;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    /**
     *
     * 메로리 회수 못하는 문제 때문에 특별한거 아니면 지역변수 사용해서 메모리 해제 할 수 있도록 한다.
     */

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object object = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제

        return object;
    }

    /**
     * 원소를 위한 공간을 적어도 하나 이상 확보한다.
     * 배열 크기를 늘려야 할 때마다 대략 두 배씩 늘린다.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
