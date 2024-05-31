package com.hodolog.hodolog.config.jwt;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class JwtAuthority implements GrantedAuthority {
    
    private final AuthorityType authorityType;

    public JwtAuthority(AuthorityType authorityType) {
        this.authorityType = authorityType;
    }

    @Override
    public String getAuthority() {
        return authorityType.name();
    }
}
