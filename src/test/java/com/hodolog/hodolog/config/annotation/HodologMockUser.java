package com.hodolog.hodolog.config.annotation;

import com.hodolog.hodolog.config.HodologMockSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


// -> 회원가입을 미리 진행하고 UserPrincipal dto를 Authentication 객체에 담아서 스프링 시큐리티 컨텍스트에 미리 넣어줄 수가 있다
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = HodologMockSecurityContext.class)
public @interface HodologMockUser {
    String name() default "호돌맨";

    String email() default "hodolman88@gmail.com";

    String password() default "";
//    String role() default "ROLE_ADMIN"; //role은 로직에서 들어간다
}
