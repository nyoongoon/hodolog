package com.hodolog.hodolog.repository.post;

import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
