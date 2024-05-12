package com.hodolog.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.hodolog.config.annotation.HodologMockUser;
import com.hodolog.hodolog.domain.Comment;
import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.repository.UserRepository;
import com.hodolog.hodolog.repository.comment.CommentRepository;
import com.hodolog.hodolog.repository.post.PostRepository;
import com.hodolog.hodolog.request.comment.CommentCreate;
import com.hodolog.hodolog.request.comment.CommentDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @AfterEach
    void clean() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    void test1() throws Exception {
        //given
        User user = User.builder()
                .name("호돌맨")
                .email("hodolman88@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
        CommentCreate request = CommentCreate.builder()
                .author("호순이")
                .password("123456")
                .content("댓글입니다. 아아아아 10글자 제한")
                .build();
        String json = objectMapper.writeValueAsString(request);
        // expected
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(1L, commentRepository.count());

        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("호순이", savedComment.getAuthor());
        assertNotEquals("123456", savedComment.getPassword());//암호화체크
        assertTrue(passwordEncoder.matches("123456", savedComment.getPassword()));
        assertEquals("댓글입니다. 아아아아 10글자 제한", savedComment.getContent());
    }

    @Test
    @DisplayName("댓글 삭제")
    void test2() throws Exception {
        //given
        User user = User.builder()
                .name("호돌맨")
                .email("hodolman88@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);
        Post post = Post.builder()
                .user(user)
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
        String encryptedPassword = passwordEncoder.encode("123456");
        Comment comment = Comment.builder()
                .author("호순이")
                .password(encryptedPassword)
                .content("댓글입니다. 아아아아 10글자 제한")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);
        CommentDelete commentDelete = new CommentDelete("123456");
        String json = objectMapper.writeValueAsString(commentDelete);
        // expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
