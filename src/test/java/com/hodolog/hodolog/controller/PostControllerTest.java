package com.hodolog.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.repository.PostRepository;
import com.hodolog.hodolog.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//@WebMvcTest // 컨트롤러의 웹 레이어 테스트 용
@AutoConfigureMockMvc // @SpringBootTest와 함께 MockMvc 주입받고 싶을 때
@SpringBootTest // 서비스, 레포가 추가되면서 전반적인 스프링 애플리케이션 테스트 필요할 때 -> mockMvc 주입이 안됨
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        // 개발자가 매우 읽기 힘든 형태
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print());
        //
    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다.")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        // 제목을 보내지 않는 경우
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        // 제목을 보내지 않는 경우
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        // then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("내용입니다.", post.getContent());
    }
}