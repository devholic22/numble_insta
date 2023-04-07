package com.numble.instagram.service;

import com.numble.instagram.dto.comment.CommentDto;
import com.numble.instagram.dto.comment.EditCommentDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.Post;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.CommentRepository;
import com.numble.instagram.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (!writer.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        Post targetPost = postRepository.findById(commentDto.getPost_id()).
                orElseThrow(() -> new NotSearchedTargetException("해당 글이 없습니다."));

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

    public Comment edit(EditCommentDto editCommentDto, Long id, User writer) {

        if (!writer.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        if (editCommentDto.getContent() == null) {
            throw new NotQualifiedDtoException("content가 비었습니다.");
        }

        Comment targetComment = commentRepository.findById(id)
                .orElseThrow(() -> new NotSearchedTargetException("해당 댓글이 없습니다."));

        if (!writer.equals(targetComment.getWriter())) {
            throw new NotPermissionException("댓글을 수정할 수 없습니다.");
        }

        targetComment.setContent(editCommentDto.getContent());

        return targetComment;
    }

    public void delete(Long id, User writer) {

        if (!writer.isActivated()) {
            throw new ExitedUserException("탈퇴했기에 권한이 없습니다.");
        }

        Comment targetComment = commentRepository.findById(id).
                orElseThrow(() -> new NotSearchedTargetException("해당 댓글이 없습니다."));

        if (!writer.equals(targetComment.getWriter())) {
            throw new NotPermissionException("댓글을 삭제할 수 없습니다.");
        }

        commentRepository.deleteById(id);
    }
}
