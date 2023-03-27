package com.numble.instagram.controller;

import com.numble.instagram.dto.CommentDto;
import com.numble.instagram.dto.DeleteCommentDto;
import com.numble.instagram.dto.EditCommentDto;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.repository.CommentRepository;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentController(CommentService commentService, UserRepository userRepository,
                             CommentRepository commentRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentDto commentDto) {
        User loggedInUser = getLoggedInUser();
        try {
            return ResponseEntity.ok(commentService.write(commentDto, loggedInUser));
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping
    public ResponseEntity<?> editComment(@RequestBody EditCommentDto editCommentDto) {
        User loggedInUser = getLoggedInUser();
        try {
            return ResponseEntity.ok(commentService.edit(editCommentDto, loggedInUser));
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentDto deleteCommentDto) {
        User loggedInUser = getLoggedInUser();
        try {
            commentService.delete(deleteCommentDto.getId(), loggedInUser);
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return userRepository.findByNickname(nickname);
    }
}
