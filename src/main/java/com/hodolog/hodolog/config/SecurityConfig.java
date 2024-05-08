package com.hodolog.hodolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.hodolog.config.handler.Http401Handler;
import com.hodolog.hodolog.config.handler.Http403Handler;
import com.hodolog.hodolog.config.handler.LoginFailHandler;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
//@EnableWebSecurity(debug = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;

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
                .authorizeHttpRequests()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/signup").permitAll()
//                .requestMatchers("/user").hasAnyRole("USER", "ADMIN") //hasAnyRole 역할 여러개 받음
                .requestMatchers("/user").hasRole("USER")
                .requestMatchers("/admin").hasRole("ADMIN") //hasRole() 에서는 ROLE_ 안붙여도 됨!
//                .access(new WebExpressionAuthorizationManager(
//                        "hasRole('ADMIN') AND hasAuthority('WRITE')")) //역할과 권한 동시에
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login") // -> http 요청 403에러 발생중 !!!
                .loginProcessingUrl("/auth/login") //로그인 비즈니스 처리 post url
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureHandler(new LoginFailHandler(objectMapper)) //로그인 실패 시 핸들러
                .and()
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                    e.authenticationEntryPoint(new Http401Handler(objectMapper)); //로그인 필요한 페이지에 로그인 없이 접근했을 로그인을 요청하게 해줌
                })
                .rememberMe(rm -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000)
                )
                // 사용자 정보 가져올 수 있는 인터페이스 사용
//                .userDetailsService(userDetailsService()) // Bean등록하면 여기에 명시안해줘도 됨
                .csrf(AbstractHttpConfigurer::disable) //todo crsf에 대해 찾아보기.
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        UserDetails user = User.withUsername("hodolman")
//                .password("1234")
//                .roles("ADMIN")
//                .build();
//        manager.createUser(user);
//        return manager;
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
