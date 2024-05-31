package com.hodolog.hodolog.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.hodolog.config.jwt.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.List;

// json 요청 방식으로 로그인하기 위한 Filter 커스텀 구현
// todo daoProvider 새로 만들어야할수도.. <<-- 비빌번호 검사하니까...
public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public EmailPasswordAuthFilter(String loginUrl, ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider) {
        super(loginUrl);
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //todo password만 빼기
        EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);


        //todo 토큰 커스텀 해보기..
        String accessToken = this.jwtTokenProvider.resolveAccessTokenByHeader(request); //헤더에 넣어주기
        String refreshToken = this.jwtTokenProvider.resolveRefreshTokenByCookie(request);

        String userEmail; //todo principal..?

        if (accessToken == null && refreshToken == null) {
//            filterChain.doFilter(request, response);
            return null; // 인증 안된 상태..
        }

        boolean isAccessTokenValidate = this.jwtTokenProvider.validateAccessToken(accessToken);
        boolean isRefreshTokenValidate = this.jwtTokenProvider.validateRefreshToken(refreshToken);

        if (isAccessTokenValidate) {
            // 엑세스 토큰 유효
//            this.setAuthenticationByAccessToken(accessToken);
            userEmail = this.jwtTokenProvider.parseClaims(accessToken, JwtTokenType.ACCESS_TOKEN).getSubject();
        } else if (isRefreshTokenValidate) {
            // 리프레시 토큰 유효
            accessToken = this.jwtTokenProvider.getAccessTokenByRefreshToken(refreshToken); //todo 미인증된 UsernamePasswordAuthenticationToken 생성중..
//            this.setAuthenticationByAccessToken(accessToken);
            this.jwtTokenProvider.addAccessTokenToCookie(response, accessToken);
            userEmail = this.jwtTokenProvider.parseClaims(accessToken, JwtTokenType.REFRESH_TOKEN).getSubject();
        } else {
            throw new TokenExpiredException();
        }


        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(userEmail, "");

        Authentication authenticate = this.getAuthenticationManager().authenticate(token);

        List<GrantedAuthority> authorities = (List) authenticate.getAuthorities();
        //todo 헤더에 토큰 넣어주기..
        // 토큰 Dto 생성
        TokenDto tokenDto = this.jwtTokenProvider.getTokens(userEmail, authorities);
        // 리프레시 토큰 엔티티 생성 및 신규 저장
        //todo db접근 로직 ... 따로 빼기..?
//        Token refreshToken = Token.of(tokenDto.getUserEmail(), tokenDto.getRefreshToken());
//        this.jwtTokenProvider.renewalToken(refreshToken); //리프레시 토큰 리뉴얼..

        // 엑세스&리프레시 토큰 쿠키에 저장
        this.jwtTokenProvider.addAccessTokenToCookie(response, tokenDto.getAccessToken());
        this.jwtTokenProvider.addRefreshTokenToCookie(response, tokenDto.getRefreshToken());
        return authenticate;
    }

//    private void setAuthenticationByAccessToken(String accessToken) {
//        Authentication auth = this.tokenProvider.getAuthentication(accessToken); // 인증 정보 가져오기 //userDetailsSerivce 접근..
//        SecurityContextHolder.getContext().setAuthentication(auth); // 시큐리티 컨텍스트에 인증 정보 담기
//    }
}
