package com.hodolog.hodolog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 에디터의 개념을 꼭 사용하지 않아도 된다 .
    public PostEditor.PostEditorBuilder toEditor() { // 빌더를 리턴
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }

    // parameter 순서, 개수 관리가 어려워질수 있음 -> toEditor() & edit() 로 변경
//    public void change(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }

    public Long getUserId(){
        return this.user.getId();
    }

    public void addComment(Comment comment) {
        comment.setPost(this);
        this.comments.add(comment);
    }
}
