package com.hodolog.hodolog.config;

import com.hodolog.hodolog.config.data.UserSession;
import com.hodolog.hodolog.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //컨트롤러에서 사용할 DTO나 어노테이션이 맞는지? 사용가능여부? 확인
        // 예시 -> UserSession DTO 생성, 활용 예시
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //컨트롤러에서 사용할 DTO나 어노테이션에 대한 값을 세팅해줌
        String accessToken = webRequest.getParameter("accessToken");
        if(accessToken == null || accessToken.equals("")){
            throw new Unauthorized();
        }

        UserSession userSession = new UserSession();
        userSession.name = accessToken;
        return userSession;
    }
}
