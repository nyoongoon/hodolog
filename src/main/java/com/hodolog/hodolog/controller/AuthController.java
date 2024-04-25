package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.request.Login;
import com.hodolog.hodolog.response.SessionResponse;
import com.hodolog.hodolog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        log.info(">>>login={}", login);
        // json 아이디/비밀번호
        // DB에서 조회
        // 토큰을 응답
        String accessToken = authService.signin(login);
        return new SessionResponse(accessToken); // 클라이언트에 json으로 내려주기 위해서 값 하나더라도 dto로 내려주기..?
    }
}
