package com.hodolog.hodolog.config;

import com.hodolog.hodolog.config.annotation.HodologMockUser;
import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class HodologMockSecurityContext implements WithSecurityContextFactory<HodologMockUser> {
    private final UserRepository userRepository;

    // test는 WithMockUser 만으로는 UserPrincipal을 알 수 없다
    // -> 회원가입을 미리 진행하고 UserPrincipal dto를 Authentication 객체에 담아서 스프링 시큐리티 컨텍스트에 미리 넣어줄 수가 있다
    @Override
    @Transactional
    public SecurityContext createSecurityContext(HodologMockUser annotation) {
        User user = User.builder()
                .name(annotation.name())
                .email(annotation.email())
                .password(annotation.password())
                .build();
        userRepository.save(user);

        // UserPrincipal > User > UserDetails
        var principal = new UserPrincipal(user);
        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        // UsernamePasswordAuthenticationToken > AbstractAuthenticationToken > Authentication
        var auth = new UsernamePasswordAuthenticationToken(
                principal,
                user.getPassword(),
                List.of(role)
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}

