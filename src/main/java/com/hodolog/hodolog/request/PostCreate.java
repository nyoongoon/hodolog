package com.hodolog.hodolog.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCreate {

    private String title;
    private String content;

}
