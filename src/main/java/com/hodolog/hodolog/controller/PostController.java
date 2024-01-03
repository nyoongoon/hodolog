package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) throws Exception {
        log.info("params={}", params.toString());
        // 데이터 검증하기 -> 데이터 검증하는 이유
        // 1. client 버그
        // 2. 보안
        // 3. db에 값 저장할 때 에러 발생 가능
        // 4. 서버 개발자의 편의를 위해서
        String title = params.getTitle();
        // 이런 검증은 소스코드 중복이 많이 일어남, 누락 가능성
        // tip -> 무언가 3번이상 반복할 떄 잘못된 게 있지 않을까 의심하기..
        if (title == null || title.equals("")) {
            // error
            throw new Exception("타이틀 값이 없습니다.");
        }
        String content = params.getContent();
        if (content == null || content.equals("")) {
            // error
        }

        return "Hello World";
    }
}
