package com.hodolog.hodolog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@Getter
@Setter
@ConfigurationProperties(prefix = "hodolman") //yml에서 설정값 읽어오기
public class AppConfig {
    private byte[] jwtKey;

    public byte[] getJwtKey() {
        return jwtKey;
    }

    public void setJwtKey(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }
}
