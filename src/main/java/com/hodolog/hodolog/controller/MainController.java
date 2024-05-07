package com.hodolog.hodolog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @PostMapping("/")
    public String main() {
        return "메인 페이지입니다.";
    }

    @GetMapping("/user")
    public String user() {
        return "사용자 페이지입니다.";
    }

    @GetMapping("/admin")
    public String admin() {
        return "관리자 페이지입니다.";
    }
}
