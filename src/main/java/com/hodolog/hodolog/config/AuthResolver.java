package com.hodolog.hodolog.config;

import com.hodolog.hodolog.config.data.UserSession;
import com.hodolog.hodolog.exception.Unauthorized;
import com.hodolog.hodolog.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private static final String KEY = "rTPmA9Sgk+Q1Xwu]bG7E6xFFUhQpdi+a151yPnRTK/Q=";
    private final AppConfig appConfig;
//    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //컨트롤러에서 사용할 DTO나 어노테이션이 맞는지? 사용가능여부? 확인
        // 예시 -> UserSession DTO 생성, 활용 예시
        return parameter.getParameterType().equals(UserSession.class); //UserSession이 있으면 resolveArgument 실행됨
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //컨트롤러에서 사용할 DTO나 어노테이션에 대한 값을 세팅해줌
//        String accessToken = webRequest.getParameter("accessToken"); //인증정보는 헤더를 통해 가져오는 것으로 수정
//        String accessToken = webRequest.getHeader("Authorization"); //인증정보 쿠키를 통해 가져오는 것으로 수정
//        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        if (servletRequest == null) {
//            log.error("servlet request is null");
//            throw new Unauthorized();
//        }
//        Cookie[] cookies = servletRequest.getCookies();
//        if (cookies.length == 0) {
//            log.error("쿠키가 없음");
//            throw new Unauthorized();
//        }
//
//        String accessToken = cookies[0].getValue();
//
//        if (accessToken == null || accessToken.equals("")) {
//            throw new Unauthorized();
//        }

        /* jwt 엑세스토큰은 db접근하지 않음! */
//        // 데이터베이스 사용자 확인 작업..
//        Session session = sessionRepository.findByAccessToken(accessToken)
//                .orElseThrow(Unauthorized::new);
//        return new UserSession(session.getUser().getId());
        /* jwt */
        String jws = webRequest.getHeader("Authorization"); //인증정보 쿠키를 통해 가져오는 것으로 수정
        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        /* jwt 복호화 */
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(appConfig.getJwtKey())
                    .build()
                    .parseClaimsJws(jws);
            log.info(">>>>>", claims);
            String userId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));
        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }
}
