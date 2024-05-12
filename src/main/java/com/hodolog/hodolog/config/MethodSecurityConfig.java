package com.hodolog.hodolog.config;

import com.hodolog.hodolog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity //기본값은 prePostEnabled=true
@RequiredArgsConstructor
public class MethodSecurityConfig {
    private final PostRepository postRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        // 메서드 익스프레션 핸들러 만들어주기
        // 시큐리티 Permission Evaluator 만들어주기
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new HodologPermissionEvaluator(postRepository));
        return handler;
    }
}
