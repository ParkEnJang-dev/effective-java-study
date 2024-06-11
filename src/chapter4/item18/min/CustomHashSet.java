package chapter4.item18.min;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomHashSet<E> extends ForwardingSet {
    private int addCount = 0;

    public CustomHashSet(Set<E> set) {
        super(set);
    }

//    public CustomHashSet(int initCap, float loadFactor) {
//        super(initCap, loadFactor);
//    }

    @Override
    public boolean add(Object o) {
        addCount++;
        System.out.println("add " + addCount);
        return super.add(o);
    }

    @Override
    public boolean addAll(Collection c) {
        addCount += c.size();
        //HashSet의 addAll 메서드는 내부적으로 add 메서드를 호출한다.
        //따라서 addCount가 중복으로 증가되는 문제가 발생한다.
        //내부 동작하는 코드를 자세히 알기 어렵다.
        //상속은 중복을 줄여주지만 캡슐화에 위배될 가능성이 있다.
        System.out.println("addAll " + addCount);
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

}
