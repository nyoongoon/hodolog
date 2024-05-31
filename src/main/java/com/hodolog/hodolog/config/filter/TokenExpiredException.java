package com.hodolog.hodolog.config.filter;

import org.springframework.security.core.AuthenticationException;

public class TokenExpiredException extends AuthenticationException {
    private final static String MESSAGE = "토큰이 만료되었습니다.";
    public TokenExpiredException() {
        super(MESSAGE);
    }
}
