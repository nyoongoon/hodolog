package com.hodolog.hodolog.repository;

import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
