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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;

// json 요청 방식으로 로그인하기 위한 Filter 커스텀 구현
// todo daoProvider 새로 만들어야할수도.. <<-- 비빌번호 검사하니까...

public class EmailPasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public EmailPasswordAuthFilter(ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider) {
        super();
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //todo jwt 검증 override
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // todo request에 토큰으로 받는지 먼저 검증 -> 토큰 없으면 로그인 로직 -> 토큰 있으면 검증로직.. --> 분리되어야하지 않나?
        EmailPassword emailPassword;
        try {
            emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String accessToken = this.jwtTokenProvider.resolveAccessTokenByHeader(request); //헤더에 넣어주기
        String refreshToken = this.jwtTokenProvider.resolveRefreshTokenByCookie(request);
        // todo 토큰 없는 경우
        if(accessToken == null && refreshToken == null){

            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(emailPassword.email, emailPassword.password);

            Authentication authenticate = this.getAuthenticationManager().authenticate(token);

            List<GrantedAuthority> authorities = (List) authenticate.getAuthorities();
            //todo 헤더에 토큰 넣어주기..
            // 토큰 Dto 생성
            TokenDto tokenDto = this.jwtTokenProvider.getTokens(emailPassword.email, authorities);
            // 리프레시 토큰 엔티티 생성 및 신규 저장
            //todo db접근 로직 ... 따로 빼기..?
//        Token refreshToken = Token.of(tokenDto.getUserEmail(), tokenDto.getRefreshToken());
//        this.jwtTokenProvider.renewalToken(refreshToken); //리프레시 토큰 리뉴얼..

            // 엑세스&리프레시 토큰 쿠키에 저장
            this.jwtTokenProvider.addAccessTokenToCookie(response, tokenDto.getAccessToken()); //쿠키저장안됨..
            this.jwtTokenProvider.addRefreshTokenToCookie(response, tokenDto.getRefreshToken());
            return authenticate; //
        }

//
        //todo 토큰 있는 경우..

        boolean isAccessTokenValidate = this.jwtTokenProvider.validateAccessToken(accessToken);
        boolean isRefreshTokenValidate = this.jwtTokenProvider.validateRefreshToken(refreshToken);


        if (isAccessTokenValidate) { //토큰에서 email꺼내서 db 검증
            // 엑세스 토큰 유효
//            this.setAuthenticationByAccessToken(accessToken);
//            userEmail = this.jwtTokenProvider.parseClaims(accessToken, JwtTokenType.ACCESS_TOKEN).getSubject();
//            EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);

        } else if (isRefreshTokenValidate) { // 프레시토큰 리뉴얼
            // 리프레시 토큰 유효
//            accessToken = this.jwtTokenProvider.getAccessTokenByRefreshToken(refreshToken); //todo 미인증된 UsernamePasswordAuthenticationToken 생성중..
////            this.setAuthenticationByAccessToken(accessToken);
//            this.jwtTokenProvider.addAccessTokenToCookie(response, accessToken);
//            userEmail = this.jwtTokenProvider.parseClaims(accessToken, JwtTokenType.REFRESH_TOKEN);
        } else {
            throw new TokenExpiredException();
        }

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(emailPassword.email, emailPassword.password);

        return  this.getAuthenticationManager().authenticate(token);
    }

//    private void setAuthenticationByAccessToken(String accessToken) {
//        Authentication auth = this.tokenProvider.getAuthentication(accessToken); // 인증 정보 가져오기 //userDetailsSerivce 접근..
//        SecurityContextHolder.getContext().setAuthentication(auth); // 시큐리티 컨텍스트에 인증 정보 담기
//    }
}
