package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.PostCreate;
import com.hodolog.hodolog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    // 데이터 검증하기 -> 데이터 검증하는 이유
    // 1. client 버그
    // 2. 보안
    // 3. db에 값 저장할 때 에러 발생 가능
    // 4. 서버 개발자의 편의를 위해서
    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate request) throws Exception {
        postService.write(request);
        return Map.of();
    }
}
