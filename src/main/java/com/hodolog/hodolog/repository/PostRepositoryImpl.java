package com.hodolog.hodolog.repository;

import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

//JpaQueryFactory 빈등록해서 주입 받아야함!
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(int page) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(10)
                .offset((long)(page - 1) * 10)
                .fetch();
    }
}
