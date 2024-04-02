package com.hodolog.hodolog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PostSearch {
    @Builder.Default //null 일경우 기본값 -> 클래스레벨 Builder
    private Integer page = 1;
    @Builder.Default
    private Integer size = 20;
}
