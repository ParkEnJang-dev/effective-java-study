# item75) 예외의 상세 메시지에 실패 관련 정보를 담으라

- 실패 순간을 포착하려면 발생한 예외에 관여된 모든 매개변수와 필드 값을 실패 메시지에 담아야 한다.
  - 보안과 관련된 비밀번호와 암호키 까지 담아선 안된다.
  - 예외 메시지엔 가독성보다 담긴 내용이 중요하다.
  - 예) IndexOutOfBoundsException 의 상세 메시지에는 최솟값, 최댓값, 벗어난 인덱스 값 을 담아야한다.

- IndexOutOfBoundsException 의 정수 인덱스 값 받는 생성자 추가.(자바9)