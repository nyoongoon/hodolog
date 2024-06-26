package com.hodolog.hodolog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
//    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

    // 인터셉터 주입 설정
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthInterceptor())
//                .excludePathPatterns("/error", "/favicon.ico", "/non-intercepted"); // 인터셉터 제외
//    }

    /**
     * 스프링 시큐리티 도입으로 불필요
     */
    // 아규먼트 리졸버 주입 설정
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new AuthResolver(appConfig)); //--> 인터셉터인증->>인증이 필요한 메소드에는 UserSession DTO를 받도록 변경
//    }


    /**
     * CORS 이슈 백엔드에서 처리 방법
     * @param registry
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173/");
//    }
}
