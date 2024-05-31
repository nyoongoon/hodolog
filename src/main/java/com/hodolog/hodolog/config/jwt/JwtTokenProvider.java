package com.hodolog.hodolog.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    // Http 프로토콜에서 헤더에 포함 되는데, 어떤 key에 토큰을 줄건지 설정
    private static final String TOKEN_HEADER = "Authorization";
    // 인증 타입 설정: jwt -> Bearer
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 30; //밀리세컨드*초*분* == 30분
    private static final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24; //밀리세컨드*초*분*시 == 24시간
    private static final String KEY_ROLES = "roles";

//    private final UserDetailsService userDetailsService;

    @Value("{spring.jwt.access-secret-key}")
    private String accessSecretKey;

    @Value("{spring.jwt.refresh-secret-key}")
    private String refreshSecretKey;

//    public JwtTokenProvider(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

    /**
     * 토큰 생성
     *
     * @param claims
     * @param secretKey
     * @param expiredTime
     * @return
     */
    private String generateToken(Claims claims, String secretKey, long expiredTime) {
        Date now = new Date();
        //jwt Token
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + expiredTime)) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 시그니처 알고리즘, 비밀키
                .compact();
    }


    // 토큰 유효성 체크
    public Claims parseClaims(String token, JwtTokenType jwtTokenType) {
        // 토큰 만료 경우 예외 발생
        String secretKey;
        if(jwtTokenType == JwtTokenType.ACCESS_TOKEN){
            secretKey = this.accessSecretKey;
        }else{ // REFRESH_TOKEN
            secretKey = this.refreshSecretKey;
        }
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰 발급
    public TokenDto getTokens(String userEmail, List<GrantedAuthority> roles) {
        // 사용자의 권한정보를 저장하기 위한 클레임 생성
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put(KEY_ROLES, roles); // 클레임은 키밸류

        //Access Token & Refresh Token
        String refreshToken = this.getRefreshTokenByClaims(claims);
        String accessToken = this.getAccessTokenByRefreshToken(refreshToken);

        return new TokenDto(userEmail, accessToken, refreshToken);
    }

    
    public String getRefreshTokenByClaims(Claims claims) {
        return this.generateToken(claims, this.refreshSecretKey, REFRESH_TOKEN_EXPIRED_TIME);
    }

    
    public String getAccessTokenByRefreshToken(String refreshToken) {
        // 사용자의 권한정보를 저장하기 위한 클레임 생성
        Claims claims = this.parseClaims(refreshToken, JwtTokenType.REFRESH_TOKEN);
        // 엑세스 토큰 재발급
        return this.generateToken(claims, accessSecretKey, ACCESS_TOKEN_EXPIRED_TIME);
    }

    // 권한 얻기

//
//    public Authentication getAuthentication(String token) {
//        String userEmail = this.parseClaims(token, this.accessSecretKey).getSubject();
//        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//
//        //스프링에서 지원해주는 형태의 토큰 -> 사용자 정보, 사용자 권한 정보
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//        public Authentication getUnauthenticatedAuthentication(String token) {
//        String userEmail = this.parseClaims(token, this.accessSecretKey).getSubject();
//
//        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(
//                userEmail,
//                "password" //todo password.. credentials 건네주기..
//        );
//        return unauthenticated;
//    }

    
    public boolean validateToken(String token, JwtTokenType jwtTokenType) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = this.parseClaims(token, jwtTokenType);
        return !claims.getExpiration().before(new Date());
    }

    
    public boolean validateAccessToken(String token) {
        return validateToken(token, JwtTokenType.ACCESS_TOKEN);
    }

    
    public boolean validateRefreshToken(String token) {
        return validateToken(token, JwtTokenType.REFRESH_TOKEN);
    }

    // 헤더에서 엑세스 토큰 얻기
    
    public String resolveAccessTokenByHeader(HttpServletRequest request) {
        String accessToken = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(accessToken) && accessToken.startsWith(TOKEN_PREFIX)) {
            return accessToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    // 쿠키에서 리프레시 토큰 얻기
    
    public String resolveRefreshTokenByCookie(HttpServletRequest request) {
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");
        if (refreshTokenCookie != null) {
            return refreshTokenCookie.getValue();
        }
        return null;
    }

    
    public void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true); // JavaScript에서 쿠키에 접근 불가능하도록 설정
        accessTokenCookie.setMaxAge(60 * 30); // 쿠키 유효 기간 설정
        response.addCookie(accessTokenCookie);
    }

    
    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true); // JavaScript에서 쿠키에 접근 불가능하도록 설정
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 유효 기간 설정 (예: 30일)
        response.addCookie(refreshTokenCookie);
    }

}
