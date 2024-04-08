package com.hodolog.hodolog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

// 비즈니스에 맞는 최상의 예외를 생성
// 추상클래스로 만든 이유 ->
@Getter
public abstract class HodologException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public HodologException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        this.validation.put(fieldName, message);
    }
}
