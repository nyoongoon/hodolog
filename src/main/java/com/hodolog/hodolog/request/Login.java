package com.hodolog.hodolog.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

/**
 * 스프링 시큐리티 도입으로 불필요
 */
//@Getter
////@Setter
////@ToString
//@NoArgsConstructor(access = AccessLevel.PROTECTED) //없으면 objectMapper json 변환 시 에러
//public class Login {
//    @NotBlank(message = "이메일을 입력해주세요.")
//    private String email;
//    @NotBlank(message = "비밀번호를 입력해주세요")
//    private String password;
//
//    @Builder
//    public Login(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }
//}
