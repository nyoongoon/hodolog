package com.hodolog.hodolog.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User { // 시큐리티의 UserDetails를 구현한 시큐리티 User를 상속

    private final Long userId;

    // 역할 != 권한
    // roles: 역할 -> 관리자, 사용자 매니저 ..
    // authority: 권한 -> 글쓰기, 글읽기, 사용자 정지..

    // 프로젝트 내에서 구현한 User -> 시큐리티 User(UserDetails)로 변환 생성
    public UserPrincipal(com.hodolog.hodolog.domain.User user) {
        super(user.getEmail(), user.getPassword(), List.of(
                // 역할이 되려면 접두어 ROLE을 붙여야함.
                new SimpleGrantedAuthority("ROLE_ADMIN")
        ));
        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
