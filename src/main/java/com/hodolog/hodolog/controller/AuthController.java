package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.domain.User;
import com.hodolog.hodolog.exception.InvalidSigninInformation;
import com.hodolog.hodolog.repository.UserRepository;
import com.hodolog.hodolog.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody Login login) {
        log.info(">>>login={}", login);
        // json 아이디/비밀번호
        // DB에서 조회
        // 토큰을 응답
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        return user;
    }
}
