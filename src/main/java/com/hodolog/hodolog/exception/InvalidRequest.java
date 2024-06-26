package com.hodolog.hodolog.exception;

/**
 * status -> 400
 */
public class InvalidRequest extends HodologException {
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message); // 예외 발생한 필드 정보 담아주기..
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
