package com.hodolog.hodolog.request;

import com.hodolog.hodolog.exception.InvalidRequest;
import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Getter
//@Setter // 불필요 --> 기본 생성자를 통해 @RequestBody를 바인딩..?
//@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    // 빌더의 장점
    // 가독성이 좋다
    // 필요한 값만 받을 수 있다
    // **객체의 불변성**
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 메시지를 가져와서 (컨트롤러 등에서) 가공하지 말고
    // 메시지를 전달해서 (validate()호출) 처리하는 것을 지향하자!
    public void validate(){
        if (this.getTitle().contains("바보")) {
            // 검증 시 예외 발생한 필드정보 (validation 정보) 담아주기..
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}
