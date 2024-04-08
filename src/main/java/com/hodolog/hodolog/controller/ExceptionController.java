package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.exception.HodologException;
import com.hodolog.hodolog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@ResponseBody // return data를 json으로 변환
public class ExceptionController {
    // status code는 @ResponseStatus(HttpStatus.NOT_FOUND) 로 설정 & 나머지 json 형태의 코드와 메시지는 바디에 담아서 보냄..!
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    @ResponseBody
//    @ResponseStatus(HttpStatus.NOT_FOUND) // http status는 이곳에서 설정 !
    // 헤더 status를 제거한다...
    @ExceptionHandler(HodologException.class)
    public ResponseEntity<ErrorResponse> hodologException(HodologException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        //@ResponseStatus를 제거하고 ResponseEntity.status()로 헤더에 상태코드 심어주기 !
        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }
}
