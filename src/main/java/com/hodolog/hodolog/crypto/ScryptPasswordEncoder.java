package com.hodolog.hodolog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("default") // profile이 default 일때만 빈등록
@Component
public class ScryptPasswordEncoder implements PasswordEncoder{
    private static final SCryptPasswordEncoder encoder = // 암호화된 비밀번호로 비교
            new SCryptPasswordEncoder(
                    16,
                    8,
                    1,
                    32,
                    64);

    @Override
    public String encrypt(String rawPassword){
        return encoder.encode(rawPassword);
    }
    @Override
    public boolean matches(String rawPassword, String encryptedPassword){
        return encoder.matches(rawPassword, encryptedPassword);
    }
}
