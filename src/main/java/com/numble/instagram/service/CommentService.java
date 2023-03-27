package com.numble.instagram.service;

import com.numble.instagram.dto.CommentDto;
import com.numble.instagram.dto.EditCommentDto;
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

    public Comment write(CommentDto commentDto, User loggedInUser) {
        Optional<Post> targetPost = postRepository.findById(commentDto.getPost_id());
        if (targetPost.isEmpty()) {
            throw new RuntimeException("해당 글이 없습니다.");
        }
        Comment newComment = Comment.builder()
                .post(targetPost.get())
                .writer(loggedInUser)
                .content(commentDto.getContent())
                .build();
        return commentRepository.save(newComment);
    }

    public Comment edit(EditCommentDto editCommentDto, User loggedInUser) {
        Optional<Comment> targetComment = commentRepository.findById(editCommentDto.getId());
        if (targetComment.isEmpty()) {
            throw new RuntimeException("해당 댓글이 없습니다.");
        }
        User writer = targetComment.get().getWriter();
        if (!writer.equals(loggedInUser)) {
            throw new RuntimeException("수정할 수 없습니다.");
        }
        targetComment.get().setContent(editCommentDto.getContent());
        return targetComment.get();
    }
}
