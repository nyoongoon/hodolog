package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.domain.Post;
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

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        // 리턴값 내려주는 경우
        // Case1. 저장한 데이터 Entity -> response로 응답하기
        // Case2. 저장한 데이터의 primary_id -> reponse로 응답하기
                    // Client에서는 수신한 id를 글 조회 API를 통해서 데이터 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST 데이터 context를 잘 관리함
        postService.write(request);
    }
}
