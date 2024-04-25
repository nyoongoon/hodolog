package com.hodolog.hodolog.response;

import lombok.Getter;

/**
 * 응답 값이 하나더라도, 클라이언트에 json 포맷으로 내려주기위해 response dto를 만드는게 좋다..?
 */
@Getter
public class SessionResponse {
    private final String accessToken;

    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
