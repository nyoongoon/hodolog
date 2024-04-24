package com.hodolog.hodolog.config;

import com.hodolog.hodolog.exception.Unauthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 인터페이스 구현으로 작성하는 방식
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    // cf) 스프링 MVC는 웹 요청을 실제로 처리하는 객체를 Handler라고 표현

    // preHandle
    // 핸들러 어탭터 가기 전에 항상 실행
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">> preHandle");
        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && accessToken.equals("hodolman")) {
            return true;
        }


        throw new Unauthorized();
        //return false인 경우 컨트롤러로 가지 않는다.
    }

    // postHandle
    // 핸들러가 어탭터 실행은 완료되었지만 아직 View가 생성되기 이전에 호출
    // The interceptor calls this method after the handler execution but before the DispatcherServlet renders the view.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info(">> postHandle - method executed");
    }

    // afterCompletion
    // 뷰가 렌더링 된 이후에 호출됨
    // 모든 View에서 최종 결과를 생성하는 일을 포함한 모든 작업이 완료된 후에 실행
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info(">> afterCompletion - Request Completed");
    }


}
