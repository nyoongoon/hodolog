package com.hodolog.hodolog.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.hodolog.config.jwt.JwtTokenProvider;
import com.hodolog.hodolog.config.jwt.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.List;


/**
 * 이렇게만 해도 잘 될 줄 알았음..
 */
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginAuthenticationFilter(ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider) {
        super("/login"); //super()가 필요로하는 로그인 경로
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        EmailPassword emailPassword;
        try {
            emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(emailPassword.email, emailPassword.password);
        // 기존 방식대로 인증 처리..
        Authentication authenticate = this.getAuthenticationManager().authenticate(token);


        List<GrantedAuthority> authorities = (List) authenticate.getAuthorities();

        TokenDto tokenDto = this.jwtTokenProvider.getTokens(emailPassword.email, authorities);
        // 리프레시 토큰 엔티티 생성 및 신규 저장
//        Token refreshToken = Token.of(tokenDto.getUserEmail(), tokenDto.getRefreshToken());
//        this.jwtTokenProvider.renewalToken(refreshToken); //리프레시 토큰 리뉴얼..

        // 엑세스&리프레시 토큰 쿠키에 저장
        this.jwtTokenProvider.addAccessTokenToCookie(response, tokenDto.getAccessToken()); //쿠키저장안됨..
        this.jwtTokenProvider.addRefreshTokenToCookie(response, tokenDto.getRefreshToken());
        return authenticate;
    }

//    /**
//     * 로그인 or 인증처리 내부에서 분기
//     */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String accessToken = this.jwtTokenProvider.resolveAccessTokenByHeader(request);
//        String refreshToken = this.jwtTokenProvider.resolveRefreshTokenByCookie(request);
//        //토큰이 둘다 없을 땐 로그인
//        if (accessToken == null && refreshToken == null) {
//            EmailPassword emailPassword;
//            try {
//                emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(emailPassword.email, emailPassword.password);
//            // 기존 방식대로 인증 처리..
//            Authentication authenticate = this.getAuthenticationManager().authenticate(token);
//
//
//            List<GrantedAuthority> authorities = (List) authenticate.getAuthorities();
//
//            TokenDto tokenDto = this.jwtTokenProvider.getTokens(emailPassword.email, authorities);
//            // 리프레시 토큰 엔티티 생성 및 신규 저장
////            Token refreshToken = Token.of(tokenDto.getUserEmail(), tokenDto.getRefreshToken());
////            this.jwtTokenProvider.renewalToken(refreshToken); //리프레시 토큰 리뉴얼..
//
//            // 엑세스&리프레시 토큰 쿠키에 저장
//            this.jwtTokenProvider.addAccessTokenToCookie(response, tokenDto.getAccessToken()); //쿠키저장안됨..
//            this.jwtTokenProvider.addRefreshTokenToCookie(response, tokenDto.getRefreshToken());
//            return authenticate;
//        }
//
//        // 인증로직
//        boolean isAccessTokenValidate = this.jwtTokenProvider.validateAccessToken(accessToken);
//        boolean isRefreshTokenValidate = this.jwtTokenProvider.validateRefreshToken(refreshToken);
//
//        if (!isAccessTokenValidate && isRefreshTokenValidate) { // 리프레시 토큰 유효
////            refreshToken = this.jwtTokenProvider.renewalRefreshToken(); // 리프레시토큰 갱신
//            accessToken = this.jwtTokenProvider.getAccessTokenByRefreshToken(refreshToken);
//            this.jwtTokenProvider.addAccessTokenToCookie(response, accessToken);
//        } else if (!isRefreshTokenValidate) {
//            throw new TokenExpiredException();
//        }
//
//        Authentication authentication = this.jwtTokenProvider.getAuthentication(accessToken);
//        return authentication;
//    }
}
