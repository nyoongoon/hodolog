package com.hodolog.hodolog;

import com.hodolog.hodolog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class) // 설정 클래스 지정 => 혹은 AppConfig에 @Configuration지정
@SpringBootApplication
public class HodologApplication {

    public static void main(String[] args) {
        SpringApplication.run(HodologApplication.class, args);
    }

}
