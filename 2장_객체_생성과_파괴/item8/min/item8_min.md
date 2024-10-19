# item 8. finalizer와 cleaner 사용을 피하라



### 설명
* finalizer와 cleaner는 예측할 수 없고, 대체로 위험하고, 일반적으로 불필요하다.
* Finalization Queue에 들어간 후 Finalizer에 의해 정리가 된다.
</br> Finalizer는 객체의 finalize 메소드를 실행한 후 메모리 정리 작업을 수행한다
</br> 보통 아래와 같이 구현해서 finalize를 사용한다.

```java
try {
    
}catch (Exception e){
    
}finally {
    super.finalize();
}
```

* cleaner는 finalizer보다는 덜 위험하지만, 여전히 예측할 수 없고 느리다.
</br> 객체의 close 메소드를 실행한 후 메모리 정리 작업을 수행한다.

### 정리
* autoCloseable을 구현하여 사용하자. 그리고 try-with-resources를 같이 사용하자.