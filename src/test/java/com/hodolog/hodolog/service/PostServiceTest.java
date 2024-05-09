package com.hodolog.hodolog.service;

import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.exception.PostNotFound;
import com.hodolog.hodolog.repository.PostRepository;
import com.hodolog.hodolog.repository.UserRepository;
import com.hodolog.hodolog.request.PostCreate;
import com.hodolog.hodolog.request.PostEdit;
import com.hodolog.hodolog.request.PostSearch;
import com.hodolog.hodolog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        User user = User.builder()
                .name("호돌맨")
                .email("hodolman88@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(user.getId(), postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requesetPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requesetPost);

        // when
        PostResponse response = postService.get(requesetPost.getId());

        // then
        assertEquals(1L, postRepository.count());
        Assertions.assertNotNull(response);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();
        // when
        List<PostResponse> posts = postService.getList(postSearch);
        // then
        assertEquals(20L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle()); // 리스트의 첫번쨰 요소가 마지막글
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();
        // when
        postService.edit(post.getId(), postEdit);
        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다"));

        assertEquals("호돌걸", changedPost.getTitle());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();
        // when
        postService.edit(post.getId(), postEdit);
        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다"));

        assertEquals("초가집", changedPost.getContent());
    }
    @Test
    @DisplayName("글 내용 삭제")
    void test6() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test7() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
        // IllegalArgumentException는 자바에서 제공하는 기본 예외이기 때문에 비즈니스를 명확히 표현해줄 수 없다. -> 커스텀 예외 만들기
//        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
//            postService.get(post.getId() + 1L);
//        });
//        // IllegalArgumentException 다른 로직에서도 사용되는 예외이기 때문에 해당 로직의 에러 메시지까지 하드코딩으로 검증했어야함 -> 커스텀 예외로 교체!
//        Assertions.assertEquals("존재하지 않는 글입니다.", e.getMessage());
        assertThrows(PostNotFound.class, ()->{
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, ()->{
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();

        // expected
        assertThrows(PostNotFound.class, ()->{
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}