package com.hodolog.hodolog.request.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Builder
@Getter
@Setter
public class PostSearch {
    private static final int MAX_SIZE = 2000;

    @Builder.Default //null 일경우 기본값 -> 클래스레벨 Builder
    private Integer page = 1;
    @Builder.Default
    private Integer size = 20;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}
