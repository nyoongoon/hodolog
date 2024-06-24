package com.hodolog.hodolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.hodolog.config.filter.JwtAuthenticationFilter;
import com.hodolog.hodolog.config.filter.LoginAuthenticationFilter;
import com.hodolog.hodolog.config.handler.Http401Handler;
import com.hodolog.hodolog.config.handler.Http403Handler;
import com.hodolog.hodolog.config.handler.LoginFailHandler;
import com.hodolog.hodolog.config.handler.LoginSuccessHandler;
import com.hodolog.hodolog.config.jwt.JwtTokenProvider;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
//@EnableWebSecurity(debug = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    /**
     * 시큐리티 무시 옵션 설정
     * - WebSecurityCustomizer
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon")
                .requestMatchers("/error")
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    /**
     * 해당 경로에 대한 권한 허용 설정, 로그인 설정
     * - SecurityFilterChain
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // AuthorizationFilter 등록
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(usernamePasswordAuthenticationFilter(jwtTokenProvider(userDetailsService(userRepository))), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRequestAuthenticationFilter(jwtTokenProvider(userDetailsService(userRepository))), LoginAuthenticationFilter.class)
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                    e.authenticationEntryPoint(new Http401Handler(objectMapper)); //로그인 필요한 페이지에 로그인 없이 접근했을 로그인을 요청하게 해줌
                })
//                .rememberMe(rm -> rm.rememberMeParameter("remember")
//                        .alwaysRemember(false)
//                        .tokenValiditySeconds(2592000)
//                )
                // 사용자 정보 가져올 수 있는 인터페이스 사용
                .csrf(AbstractHttpConfigurer::disable) //todo crsf에 대해 찾아보기.
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) <<-- 여기서 시도해도 안됨!
                .build();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(UserDetailsService userDetailsService) {
        return new JwtTokenProvider(userDetailsService);
    }

    @Bean
    public JwtAuthenticationFilter jwtRequestAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenProvider);
        return filter;
    }

    //json 로그인 방식 요청을 받기위한 필터 생성
    @Bean
    public LoginAuthenticationFilter usernamePasswordAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter(objectMapper, jwtTokenProvider);
        filter.setAuthenticationManager(authenticationManager(daoAuthenticationProvider()));
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        // 인증이 유효하도록 만들어주는 컨텍스트 -> 이것이 있어야 세션 발급됨
//        HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();
//        filter.setSecurityContextRepository(httpSessionSecurityContextRepository);
        return filter;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider daoAuthenticationProvider) { //filter에 AuthenticationManager 넘겨주기 위한 Bean
        return new ProviderManager(daoAuthenticationProvider); //provider를 넘겨주기
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider jwtDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));
        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // DB로 관리하기
        return username -> {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
            return new UserPrincipal(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder(
                16,
                8,
                1,
                32,
                64);
    }
}
