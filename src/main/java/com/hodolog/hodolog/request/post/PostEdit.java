package com.hodolog.hodolog.request.post;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

/**
 * PostEdit 생성 시점에 PostCreate와 내용이 같더라도
 * 기능이 다르면 구별하여 생성하는 것이 옳다.
 * 기능이 다르므로 추후 차이점이 생길 가능성이 매우 높으므로
 */
@Getter
//@Setter
//@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
