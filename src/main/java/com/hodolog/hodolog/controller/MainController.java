package com.hodolog.hodolog.controller;

import com.hodolog.hodolog.config.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @PostMapping("/")
    public String main() {
        return "메인 페이지입니다.";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal UserPrincipal userPrincipal) { // 들어올 떄 AuthenticationProviderManager가 비밀번호 삭제하여 null로 만듬
        return "사용자 페이지입니다.";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "관리자 페이지입니다.";
    }
}
