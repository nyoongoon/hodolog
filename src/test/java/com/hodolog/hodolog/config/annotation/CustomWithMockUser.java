package com.hodolog.hodolog.config.annotation;

import com.hodolog.hodolog.config.MocksUserFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MocksUserFactory.class)
public @interface CustomWithMockUser {
    String username() default "hodolman88@gmail.com";
    String password() default "1234";
    int level() default 5;
    String mobileNumber() default "01000000000";
}
