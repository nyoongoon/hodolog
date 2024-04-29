package com.hodolog.hodolog.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //없으면 objectMapper json 변환 시 에러
public class Signup {
    private String email;
    private String password;
    private String name;

    @Builder
    public Signup(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
