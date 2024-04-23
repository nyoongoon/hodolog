package com.hodolog.hodolog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 인터페이스 구현으로 작성하는 방식
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    // preHandle
    // 핸들러 가기 전에 항상 실행
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">> preHandle");
        return true;
    }

    // postHandle
    // 핸들러가 실행되고 나서 뷰 반환하고 실행됨.. afterCompletion과 차이점?
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info(">> postHandle");
    }

    // afterCompletion
    // 뷰 반환 끝내고 나서 실행됨
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info(">> afterCompletion");
    }




}
