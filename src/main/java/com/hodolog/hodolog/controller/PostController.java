package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    // 데이터 검증하기 -> 데이터 검증하는 이유
    // 1. client 버그
    // 2. 보안
    // 3. db에 값 저장할 때 에러 발생 가능
    // 4. 서버 개발자의 편의를 위해서
    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) throws Exception {
        //이 검증 방식의 문제점
        //1. 메서드마다 값을 검증 해야함.
        //2. 응답값에 HashMap -> 응답 클래스를 만드는 것이 좋음
        //3. 여러개의 에러처리 힘듬
        //4. 세 번 이상의 반복작업은 피하자..!
        if(result.hasErrors()){
            List<FieldError> fieldErrorList = result.getFieldErrors();
            FieldError firstFieldError = fieldErrorList.get(0);
            String fieldName = firstFieldError.getField(); // title
            String errorMessage = firstFieldError.getDefaultMessage(); // ..에러메시지
            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }

        return Map.of();
    }
}
