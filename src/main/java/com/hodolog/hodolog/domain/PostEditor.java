package com.hodolog.hodolog.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * 프론트에서 null값을 입력 받은경우 기존 엔티티의 값을 유지할 수 있게 도와주는 클래스 -> 빌더 패턴 직접 사용
 */
@Getter
public class PostEditor { // Editor 클래스는 수정을 할 수 있는 필드들에 대해서만 따로 정리
    private final String title; // Editor 안에 비즈니스에 관련된 필드만 따로 선언하기 때문에 관리가 쉬워짐
    private final String content;

    @Builder
    public PostEditor(String title, String content) {
        // 이곳에서 null 체크는 무의미 -> post.toEditor해도 this.title의 값이 들어가지는 건 아니다(null)
        // post.toEditor할 떄 들어가는 곳은 PostEditorBuilder 클래스의 필드로 들어가는 것!! --> PostEditorBuilder.title()에서 체크
//        if(title != null){
//            this.title = title;
//        }
        this.title = title;
        this.content = content;
    }

    public static PostEditorBuilder builder() {
        return new PostEditorBuilder();
    }

    // null값 입력 체크를 위한 Builder 클래스별도 생성
    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditorBuilder title(final String title) {
            if (title != null) { // 이곳에서 null체크를 해야 null 값을 입력받은 경우 기존값 유지 가능
                this.title = title;
            }
            return this;
        }

        public PostEditorBuilder content(final String content) {
            if (content != null) {// 이곳에서 null체크를 해야 null 값을 입력받은 경우 기존값 유지 가능
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
