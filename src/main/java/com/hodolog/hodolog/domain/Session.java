package com.hodolog.hodolog.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * 스프링 시큐리티 도입으로 불필요 -> 스프링 시큐리티에서 제공해주는 세션 사용
 */
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Session {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String accessToken;
//
//    @ManyToOne
//    private User user;
//
//    @Builder
//    public Session(User user) {
//        this.accessToken = UUID.randomUUID().toString();
//        this.user = user;
//    }
//}
