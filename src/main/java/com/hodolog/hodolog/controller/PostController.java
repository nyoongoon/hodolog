package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.PostCreate;
import com.hodolog.hodolog.request.PostSearch;
import com.hodolog.hodolog.response.PostResponse;
import com.hodolog.hodolog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        // 요청 클래스 -> 요청 + 밸리데이션 정책
        // 응답 클래스 -> 응답 + 서비스 정책 일부
        return postService.get(postId);
    }

    @GetMapping("/posts") // querydsl을 이용하여 페이징 처리하기...
    public List<PostResponse> getList(@RequestParam PostSearch pageable){

        return postService.getList(pageable);
    }
}
