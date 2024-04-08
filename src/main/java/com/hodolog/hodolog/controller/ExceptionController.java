package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.exception.PostNotFound;
import com.hodolog.hodolog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
@ResponseBody // return data를 jsom으로 변환.;
public class ExceptionController {

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
    @ResponseStatus(HttpStatus.NOT_FOUND) // http status는 이곳에서 설정 !
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse postNotFound(PostNotFound e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("404")
                .message(e.getMessage())
                .build();

        return response;
    }
}
