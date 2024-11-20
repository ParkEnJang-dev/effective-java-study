# 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라.



- 마커 애너테이션 vs 마커 인터페이스 
  - 안정성: 마커 인터페이스는 컴파일 타임에 타입 검사가 가능하므로 타입 안정성을 보장합니다. 반면, 마커 애너테이션은 런타임에 리플렉션을 사용하여 확인합니다.
  - 메타데이터 vs 타입 정보: 마커 애너테이션은 주로 메타데이터(정보)를 전달하고, 마커 인터페이스는 객체의 타입을 나타냅니다.
  - 간편한 추가: 애너테이션은 클래스나 메서드에 쉽게 붙일 수 있으므로 마커 인터페이스보다 범용적으로 사용하기 좋다

```java
public interface Permission {
    // 메서드가 없고, 단지 특정 타입을 나타내는 역할만 함
}

public class AdminPermission implements Permission {
    // 관리자 권한을 나타내는 클래스
}

public class UserPermission implements Permission {
    // 사용자 권한을 나타내는 클래스
}

```

- 전달받은 객체가 Permission 타입인지 검사하고, 권한에 따라 특정 작업을 수행할 수 있도록 한다.

```java
public class PermissionChecker {
    public static void checkPermission(Permission permission) {
        if (permission instanceof AdminPermission) {
            System.out.println("관리자 권한이 확인되었습니다. 관리자 기능을 수행합니다.");
            // 관리자만 수행할 수 있는 작업 코드
        } else if (permission instanceof UserPermission) {
            System.out.println("사용자 권한이 확인되었습니다. 일반 기능을 수행합니다.");
            // 사용자만 수행할 수 있는 작업 코드
        } else {
            System.out.println("권한이 없습니다.");
        }
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Permission adminPermission = new AdminPermission();
        Permission userPermission = new UserPermission();

        PermissionChecker.checkPermission(adminPermission);  // 관리자 권한이 확인되었습니다.
        PermissionChecker.checkPermission(userPermission);   // 사용자 권한이 확인되었습니다.
    }
}
```


### 정리
- 새로 추가 하는 메서드 없이 단지 타입 목적이라면 마커 인터페이스를 선택하라.
- 클래스나 인터페이스 외의 프로그램 요소에 마킹해야 하거나, 애너테이션을 적극 활용하는 프레임워크의 일부로 그 마커를 편입시키고자 한다면 마커 애너테이션이 올바른 선택이다.
