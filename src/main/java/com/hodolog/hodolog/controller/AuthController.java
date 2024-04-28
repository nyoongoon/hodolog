package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.Login;
import com.hodolog.hodolog.response.SessionResponse;
import com.hodolog.hodolog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final String KEY = "rTPmA9Sgk+Q1Xwu]bG7E6xFFUhQpdi+a151yPnRTK/Q=";
    private final AuthService authService;

    @PostMapping("/auth/login")
//    public SessionResponse login(@RequestBody Login login) { //쿠키로 대체
//    public ResponseEntity<Object> login(@RequestBody Login login) { // jwt로 대체
    public SessionResponse login(@RequestBody Login login) {
        log.info(">>>login={}", login);
        // json 아이디/비밀번호
        // DB에서 조회
        // 토큰을 응답
//        String accessToken = authService.signin(login);

        // 응답바디로 내려주는 방식보다(클라에서는 헤더에 담아서) -> 쿠키로 내려주고 쿠키로 요청받는 인증이 추세
//        return new SessionResponse(accessToken); // 클라이언트에 json으로 내려주기 위해서 값 하나더라도 dto로 내려주기..?

        // HttpServletRequest에 쿠키 담아도 괜찮지만 다른 방식이 있음
//        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost") //todo 서버환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//
//        log.info(">>>> cookie={}", cookie.toString());
//
//        return ResponseEntity.ok() // 쿠키는 헤더에 담아서 내려줌 -> Set-Cookie로 내려줘서 클라이언트가 쿠키로 설정할 수 있도록.
//                .header(HttpHeaders.SET_COOKIE, cookie.toString()) // 이렇게하면 브라우저가 쿠키 자동으로 담음
//                .build();

        Long userId = authService.signin(login);
        /* jwt 인증으로 변경 */
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));
        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .compact();

        return new SessionResponse(jws);
    }
}
