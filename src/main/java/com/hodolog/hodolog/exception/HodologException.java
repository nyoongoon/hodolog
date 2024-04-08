package com.hodolog.hodolog.exception;

// 비즈니스에 맞는 최상의 예외를 생성
// 추상클래스로 만든 이유 ->
public abstract class HodologException extends RuntimeException {
    public HodologException(String message) {
        super(message);
    }

    public HodologException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
