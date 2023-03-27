package com.numble.instagram.service;

import com.numble.instagram.dto.CommentDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.Post;
import com.numble.instagram.entity.User;
import com.numble.instagram.repository.CommentRepository;
import com.numble.instagram.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Comment write(CommentDto commentDto, User writer) {
        Optional<Post> targetPost = postRepository.findById(commentDto.getPost_id());
        if (targetPost.isEmpty()) {
            throw new RuntimeException("해당 글이 없습니다.");
        }
        Comment newComment = Comment.builder()
                .post(targetPost.get())
                .writer(writer)
                .content(commentDto.getContent())
                .build();
        return commentRepository.save(newComment);
    }
}
