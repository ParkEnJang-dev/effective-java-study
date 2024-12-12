# ITEM60) 정확한 답이 필요하다면 float와 double은 피하라

- float와 double 은 넓은 범위의 수를 빠르게 정밀한 ‘근사치’로 계산하도록 설계 됨.
    
    → 따라서 정확한 결과가 필요할 때는 사용하면 안된다.
    
    ```java
    System.out.println(1.03 - 0.42);
    // 0.61 이 아니라 0.61000000000000001 출력
    ```
    
- 금융 계산에는 BigDecimal, int 혹은 long을 사용해야 한다.
- BigDecimal 사용
    - 여덟가지 반올림 모드를 이용하여 반올림을 완벽히 제어할 수 있다.
    - 기본 타입보다 쓰기가 훨씬 불편하고, 느리다.
    
    ```java
    package java.math;
    
    public enum RoundingMode {
        
        // 0에서 멀어지는 방향으로 올림 
        // 양수인 경우엔 올림, 음수인 경우엔 내림
        UP(BigDecimal.ROUND_UP),
        
        // 0과 가까운 방향으로 내림
        // 양수인 경우엔 내림, 음수인 경우엔 올림
        DOWN(BigDecimal.ROUND_DOWN),
        
        // 양의 무한대를 향해서 올림 (올림)
        CEILING(BigDecimal.ROUND_CEILING),
        
        // 음의 무한대를 향해서 내림 (내림)
        FLOOR(BigDecimal.ROUND_FLOOR),
        
        // 반올림 (사사오입) 
        // 5 이상이면 올림, 5 미만이면 내림
        HALF_UP(BigDecimal.ROUND_HALF_UP),
        
        // 반올림 (오사육입) 
        // 6 이상이면 올림, 6 미만이면 내림
        HALF_DOWN(BigDecimal.ROUND_HALF_DOWN),
    
        // 반올림 (오사오입, Bankers Rounding)
        // 5 초과면 올리고 5 미만이면 내림, 5일 경우 앞자리 숫자가 짝수면 버리고 홀수면 올림하여 짝수로 만듦
        HALF_EVEN(BigDecimal.ROUND_HALF_EVEN),
        
        // 소수점 처리를 하지 않음
        // 연산의 결과가 소수라면 ArithmeticException이 발생함
        UNNECESSARY(BigDecimal.ROUND_UNNECESSARY);
    }
    ```
    
- BigDecimal 대안으로 int 혹은 long 타입 사용
    - 다룰 수 있는 값의 크기가 제한되고, 소수점을 직접 관리해야 한다.