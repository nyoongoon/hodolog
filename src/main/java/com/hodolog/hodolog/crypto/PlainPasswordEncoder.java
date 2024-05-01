package com.hodolog.hodolog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 스프링 시큐리티 도입으로 불필요
 */
/**
 * test 시 다른 빈 받을 수 있다는 참고용 - 반드시 패스워드 설정이 운영 / 테스트 달라야 할 필요는 없는듯..
 */
//@Profile("test") // profile이 test 일때만 빈등록
//@Component
//public class PlainPasswordEncoder implements PasswordEncoder{
//    @Override
//    public String encrypt(String rawPassword) {
//        return rawPassword;
//    }
//
//    @Override
//    public boolean matches(String rawPassword, String encryptedPassword) {
//        return rawPassword.equals(encryptedPassword);
//    }
//}
