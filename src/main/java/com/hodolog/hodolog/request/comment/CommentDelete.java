package com.hodolog.hodolog.request.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDelete {
    private String password;

    public CommentDelete(String password) {
        this.password = password;
    }
}
