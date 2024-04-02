package com.hodolog.hodolog.repository;

import com.hodolog.hodolog.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(int page);
}
