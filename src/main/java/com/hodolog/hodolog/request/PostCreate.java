package com.hodolog.hodolog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCreate {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

}
