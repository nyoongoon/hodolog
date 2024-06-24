package com.hodolog.hodolog.config.filter;

import com.hodolog.hodolog.config.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter -> 한 요청당 한번 필터 실행..

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = this.jwtTokenProvider.resolveAccessTokenByHeader(request);
        String refreshToken = this.jwtTokenProvider.resolveRefreshTokenByCookie(request);
        if (accessToken == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰검증로직
        boolean isAccessTokenValidate = this.jwtTokenProvider.validateAccessToken(accessToken);
        boolean isRefreshTokenValidate = this.jwtTokenProvider.validateRefreshToken(refreshToken);

        if (isAccessTokenValidate) {
            log.info("[엑세스 토큰 검증 성공]");
        } else if (isRefreshTokenValidate) { // 리프레시 토큰 유효
            refreshToken = this.jwtTokenProvider.renewalRefreshToken(); // 리프레시토큰 갱신
            accessToken = this.jwtTokenProvider.getAccessTokenByRefreshToken(refreshToken);
            this.jwtTokenProvider.addAccessTokenToCookie(response, accessToken);
            log.info("[리프레시 토큰 검증 성공]");
        } else if (!isRefreshTokenValidate) {
            throw new TokenExpiredException();
        }
        // stateless한 서버를 사용한다 -> 요청 마다 토큰 검증하여 시큐리티 컨텍스트에 인증 객체 넣고, 요청 끝나면 삭제
        Authentication authentication = this.jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

//        Authentication authenticated = SecurityContextHolder.getContext().getAuthentication();
//
//
//        return authentication;
//
////        Authentication authentication = this.jwtTokenProvider.getAuthentication(accessToken);


//        SecurityContextHolder.getContext().setAuthentication(authentication); // 시큐리티 컨텍스트에 인증 정보 담기
//        filterChain.doFilter(request, response);
//    }

//    private void setAuthenticationByAccessToken(String accessToken) {
//        Authentication auth = this.jwtTokenProvider.getAuthentication(accessToken); // 인증 정보 가져오기
//        SecurityContextHolder.getContext().setAuthentication(auth); // 시큐리티 컨텍스트에 인증 정보 담기
//    }
}
