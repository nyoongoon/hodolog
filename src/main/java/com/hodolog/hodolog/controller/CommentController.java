package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.comment.CommentCreate;
import com.hodolog.hodolog.request.comment.CommentDelete;
import com.hodolog.hodolog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable Long postId, @RequestBody @Valid CommentCreate request) {
        // CommentCreate는 웹 영역의 dto -> 서비스로 넘길때 서비스 영역의 dto로 만들어 넘기는 것이 더 좋은 방법..
        commentService.write(postId, request);
    }

    /**
     * Http DELETE Request Body 이슈 - Delete는 requestBody 전달하지 않는다는 규약이 있음
     *
     * @param commentId
     * @param request
     */
//    @DeleteMapping("/comments/{commentId}")
    @PostMapping("/comments/{commentId}/delete")
    public void delete(@PathVariable Long commentId, @RequestBody @Valid CommentDelete request) {
        commentService.delete(commentId, request);
    }
}
