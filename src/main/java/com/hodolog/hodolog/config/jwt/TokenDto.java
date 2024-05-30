package com.hodolog.hodolog.config.jwt;

/**
 * 토큰 Dto
 */
public class TokenDto { //TODO Request vs Response 구분
    private String userEmail;
    private String accessToken;
    private String refreshToken;

    public TokenDto(String userEmail, String accessToken, String refreshToken) {
        this.userEmail = userEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
