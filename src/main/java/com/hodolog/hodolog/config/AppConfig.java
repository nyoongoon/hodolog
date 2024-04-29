package com.hodolog.hodolog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "hodolman") //yml에서 설정값 읽어오기
public class AppConfig {
}
