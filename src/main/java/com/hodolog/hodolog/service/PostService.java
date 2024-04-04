package com.hodolog.hodolog.service;

import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.domain.PostEditor;
import com.hodolog.hodolog.repository.PostRepository;
import com.hodolog.hodolog.request.PostCreate;
import com.hodolog.hodolog.request.PostEdit;
import com.hodolog.hodolog.request.PostSearch;
import com.hodolog.hodolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        return postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        // 응답 클래스 분리
        // 엔티티 -> 응답 클래스 분리 작업을 이곳(서비스)에서 하는 것이 맞을까??
        // 서비스 계층을 2가지로 분리하는 것도 좋은 방법

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 문제점 -> 글이 너무 많은 경우 -> 비용이 많이 든다
    // 글이 1억개 -> DB글을 모두 조회하는 경우 -> DB가 뻗을 수도 있음 -> 페이징
    // 커스텀 요청 DTO를 통해 추후 정렬 등의 기능도 담을 수 있도록 만든다
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));

//        post.change(postEdit.getTitle(), postEdit.getContent());
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor(); // 엔티티에서 빌더를 받아서 빌더를 빌드한 후 edit() 메소드에 넣기
        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit(postEditor);

    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다"));
        postRepository.delete(post);

    }
}
