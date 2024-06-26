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

import java.util.HashMap;

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
                .validation(new HashMap<>()) //기본에러타입일 경우에도 validation 필드 추가..
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
                .validation(e.getValidation()) // 밸리데이션 추가하기 -> 에러가 발생한 필드 정보 담아주는 것 필요..
                .build();

        // 응답 json validation -> title : 제목에 바보를 포함할 수 없습니다.

        //@ResponseStatus를 제거하고 ResponseEntity.status()로 헤더에 상태코드 심어주기 !
        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e){
        log.error("예외발생", e);

        ErrorResponse body = ErrorResponse.builder()
                .code("500")
                .message(e.getMessage())
                .build();


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(500)
                .body(body);

        return response;
    }
}
