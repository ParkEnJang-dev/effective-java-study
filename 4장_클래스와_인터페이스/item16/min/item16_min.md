# item 16. public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라




아래처럼 public 클래스에서는 public 필드를 사용하지 말아야 한다.<br/>
접근자를 통해서 데이터를 변경하도록 한다.

``` java

 class Point {
  private double x;
  
  public Point(double x) {
    this.x = x;
  }
  
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
 
 }

```



