package com.hodolog.hodolog.repository.comment;

import com.hodolog.hodolog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
