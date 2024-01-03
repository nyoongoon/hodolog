package com.hodolog.hodolog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    void test() throws Exception {
        // 글 제목
        // 글 내용

        // mockMvc 기본적으로 contentType을 application/json 형태로 보
        // key&value 형태인 application/x-www-urlencorded는 중첩된 데이터 형태를 표현하기 힘듦
        // ->> application/json을 많이 사용하는 추세

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
        //
    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다.")
    void test2() throws Exception {
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        // 제목을 보내지 않는 경우
                        .content("{\"title\": \"\", \"content\": \"내용입니다.\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());

    }
}