package com.numble.instagram.service;

import com.numble.instagram.dto.comment.CommentDto;
import com.numble.instagram.dto.comment.EditCommentDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.Post;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.NotLoggedInException;
import com.numble.instagram.exception.NotQualifiedDtoException;
import com.numble.instagram.exception.NotSearchedTargetException;
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

        if (writer == null) {
            throw new NotLoggedInException("로그인되지 않았습니다.");
        }

        Post targetPost = postRepository.findById(commentDto.getPost_id()).orElseThrow(
                () -> new NotSearchedTargetException("해당 글이 없습니다."));

        if (commentDto.getPost_id() == null || commentDto.getContent() == null) {
            throw new NotQualifiedDtoException("post_id 또는 content가 비었습니다.");
        }

        Comment newComment = Comment.builder()
                .post(targetPost)
                .writer(writer)
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

    public void delete(Long commentId, User loggedInUser) {
        Optional<Comment> targetComment = commentRepository.findById(commentId);
        System.out.println(targetComment.isEmpty());
        if (targetComment.isEmpty()) {
            throw new RuntimeException("해당 댓글이 없습니다.");
        }
        User writer = targetComment.get().getWriter();
        if (!writer.equals(loggedInUser)) {
            throw new RuntimeException("삭제할 수 없습니다.");
        }
        commentRepository.deleteById(commentId);
    }
}
