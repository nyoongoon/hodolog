package com.hodolog.hodolog.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor { // Editor 클래스는 수정을 할 수 있는 필드들에 대해서만 따로 정리
    private final String title; // Editor 안에 비즈니스에 관련된 필드만 따로 선언하기 때문에 관리가 쉬워짐
    private final String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
