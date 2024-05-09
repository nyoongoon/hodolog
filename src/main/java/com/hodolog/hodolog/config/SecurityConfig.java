package com.hodolog.hodolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.hodolog.config.filter.EmailPasswordAuthFilter;
import com.hodolog.hodolog.config.handler.Http401Handler;
import com.hodolog.hodolog.config.handler.Http403Handler;
import com.hodolog.hodolog.config.handler.LoginFailHandler;
import com.hodolog.hodolog.config.handler.LoginSuccessHandler;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

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
                .authorizeHttpRequests()
//                .requestMatchers("/auth/login").permitAll()
//                .requestMatchers("/auth/signup").permitAll()
//                .requestMatchers("/user").hasAnyRole("USER", "ADMIN") //hasAnyRole 역할 여러개 받음
//                .requestMatchers("/user").hasRole("USER") -> 메서드 시큐리티 적용
//                .requestMatchers("/admin").hasRole("ADMIN") //hasRole() 에서는 ROLE_ 안붙여도 됨!
//                .access(new WebExpressionAuthorizationManager(
//                        "hasRole('ADMIN') AND hasAuthority('WRITE')")) //역할과 권한 동시에
//                .anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) //json 로그인 처리 필터 삽입
//                    .formLogin() //json 요청 방식 로그인으로 대체
//                    .loginPage("/auth/login") // -> http 요청 403에러 발생중 !!!
//                    .loginProcessingUrl("/auth/login") //로그인 비즈니스 처리 post url
//                    .usernameParameter("username")
//                    .passwordParameter("password")
//                    .defaultSuccessUrl("/")
//                    .failureHandler(new LoginFailHandler(objectMapper)) //로그인 실패 시 핸들러
//                .and()
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

    //json 로그인 방식 요청을 받기위한 필터 생성

    @Bean
    public EmailPasswordAuthFilter usernamePasswordAuthenticationFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        // AbstractAuthenticationProcessingFilter에서 가지고 있던 것들을 커스텀
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        // 실제로 인증이 완료 됐을 때 요청 내에서 인증이 유효하도록 만들어주는 컨텍스트 -> 이것이 있어야 세션 발급됨
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        //remember-me
        //todo 리멤버미 쿠키에 저장 안된듯?
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }
    @Bean
    public AuthenticationManager authenticationManager() { //filter에 AuthenticationManager 넘겨주기 위한 Bean
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider); //provider를 넘겨주기
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
