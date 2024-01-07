package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
//        if (e.hasErrors()) {
//            FieldError fieldError = e.getFieldError();
//            String field = fieldError.getField();
//            String message = fieldError.getDefaultMessage();
        return new ErrorResponse("400", "잘못된 요청입니다.");
//        }


    }
}
