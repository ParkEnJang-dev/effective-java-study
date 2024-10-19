# item 13. clone 재정의는 주의해서 진행하라



### 정리
* 불변 클래스에서는 굳이 clone 메서드를 제공하지 않는게 좋다. (super.clone()으로 해결.

* clone 메서드는 인스턴스를 복제하는 메서드이다. 이 메서드를 사용하기 위해서는 반드시 Cloneable 인터페이스를 implements 해야한다. 이를 어길 경우 clone 메서드 호출 시 CloneNotSupportedException이 발생한다.

* 복제 는 생성자와 팩터리를 이용하고 , 배열은 clone 메서드를 이용하자.