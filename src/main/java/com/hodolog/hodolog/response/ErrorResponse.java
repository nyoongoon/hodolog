package com.hodolog.hodolog.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * {
 *     "code" : "400",
 *     "message" : "잘못된 요청입니다.",
 *     "validation" : {
 *         "title" : "값을 입력해주세요."
 *     }
 *
 * }
 */
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
}
