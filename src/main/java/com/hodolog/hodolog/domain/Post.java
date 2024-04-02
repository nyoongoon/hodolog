package com.hodolog.hodolog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // TODO 엔티티의 GETTER에는 서비스의 정책을 절대 넣지 말 것! -> 응답 클래스 분리
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor(){ // 빌더를 리턴
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor){
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }

    // parameter 순서, 개수 관리가 어려워질수 있음 -> toEditor() & edit() 로 변경
//    public void change(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
}
