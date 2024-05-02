package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.PostCreate;
import com.hodolog.hodolog.request.PostEdit;
import com.hodolog.hodolog.request.PostSearch;
import com.hodolog.hodolog.response.PostResponse;
import com.hodolog.hodolog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 시큐리티 도입으로 불필요
     */
//    @GetMapping("/foo") //@RequestAttribute사용예시: 인터셉터->컨트롤러 값 설정가능
//    public String foo(@RequestAttribute("userName") String userName){
//    @GetMapping("/foo")
//    public Long foo(UserSession userSession){
//        log.info(">>> {}", userSession.id);
//        return userSession.id;
//    }

    @GetMapping("/non-intercepted")
    public String nonIntercepted(){
        return "nonIntercepted";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        // 리턴값 내려주는 경우
        // Case1. 저장한 데이터 Entity -> response로 응답하기
        // Case2. 저장한 데이터의 primary_id -> reponse로 응답하기
        // Client에서는 수신한 id를 글 조회 API를 통해서 데이터 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST 데이터 context를 잘 관리함

        // 디테일한 검증이 필요한 경우
//        if (request.getTitle().contains("바보")) {
//            throw new InvalidRequest();
//        }
        // 하지만 위처럼 메시지를 가져와서 가공하는 등의 작업은 지양하기 -> **메시지를 던지는 방향으로** 리팩토링!
        request.validate(); // 검증 방식 주의 -> 가져오지 말고 던지기 !!!ㅎ

        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        // 요청 클래스 -> 요청 + 밸리데이션 정책
        // 응답 클래스 -> 응답 + 서비스 정책 일부
        return postService.get(postId);
    }

    @GetMapping("/posts") // querydsl을 이용하여 페이징 처리하기...
    //@ModalAttribute -> 쿼리파라미터로 던저도 dto로 받는다.
    public List<PostResponse> getList(@ModelAttribute PostSearch pageable) {

        return postService.getList(pageable);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        // 클라이언트에서 결과 리턴을 요구할 때도 있음.
        postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {

    }
}
