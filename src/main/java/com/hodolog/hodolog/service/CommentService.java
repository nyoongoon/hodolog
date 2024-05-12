package com.hodolog.hodolog.service;

import com.hodolog.hodolog.domain.Comment;
import com.hodolog.hodolog.domain.Post;
import com.hodolog.hodolog.exception.CommentNotFound;
import com.hodolog.hodolog.exception.InvalidPassword;
import com.hodolog.hodolog.exception.PostNotFound;
import com.hodolog.hodolog.repository.comment.CommentRepository;
import com.hodolog.hodolog.repository.post.PostRepository;
import com.hodolog.hodolog.request.comment.CommentCreate;
import com.hodolog.hodolog.request.comment.CommentDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void write(Long postId, CommentCreate request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        post.addComment(comment);
    }

    //todo delete에는 @transacational 달지 않는 이유 찾아보기..
    public void delete(Long commentId, CommentDelete request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        String encrypedPassword = comment.getPassword();
        if(passwordEncoder.matches(request.getPassword(), encrypedPassword)){
            throw new InvalidPassword();
        }
        commentRepository.delete(comment);
    }
}
