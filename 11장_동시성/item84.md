# ITEM84) 프로그램의 동작을 스레드 스케줄러에 기대지 마라

- 정확성이나 성능이 스레드 스케줄러에 따라 달라지는 프로그램이라면 다른 플랫폼에 이식하기 어렵다

### 이식성 좋은 프로그램 작성 방법

- 실행 가능한 스레드의 평균적인 수를 프로세서 수보다 지나치게 많아지지 않도록 하는 것
- 실행 준비가 된 스레드들은 맡은 작업을 완료할 때까지 계속 실행되도록 하자

### 실행 가능한 스레드 수를 적게 유지하자

- 스레드는 당장 처리해야 할 작업이 없다면 실행돼서는 안 된다.
- 스레드는 절대 바쁜 대기(busy waiting) 상태가 되면 안 된다.
    
    → 스레드 스케줄러의 변덕에 취약할 뿐 아니라 프로세서에 큰 부담을 주어 다른 유용한 작업이 실행될 기회를 박탈한다.
    
    ```java
    // 끔찍한 CountDownLatch 구현 - 바쁜 대기 버전
    public class SlowCountDownLatch {
    	private int count;
    	
    	public SlowCountDownLatch(int count) {
    		if(count < 0)
    			throw new IllegalArgumentException(count + " <0");
    		this.count = count;
    	}
    	
    	public void await() {
    		while(true) { //무한루프 조건 충족 검사
    			synchronized(this) 
    				if (count ==0 )
    					return;
    			}
    		}
    	}
    	
    	public synchronized void countDown() {
    		if (count != 0)
    			count--;
    	}
    }
    ```
    

### Thread.yield 사용 지양

- JVM의 스레드 스케줄러에게 현재 스레드의 우선 순위를 낮춰도 된다는 힌트를 보내는 메서드
- Thread.yield는 테스트할 수단도 없다.
- 애플리케이션 구조를 바꿔 동시에 실행 가능한 스레드 수가 적어지도록 조치해주자.