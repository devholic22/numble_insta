package com.numble.instagram.controller;

import com.numble.instagram.dto.comment.CommentDto;
import com.numble.instagram.dto.comment.DeleteCommentDto;
import com.numble.instagram.dto.comment.EditCommentDto;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.CommentRepository;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.CommentService;
import com.numble.instagram.util.UserUtil;
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
    private final UserUtil userUtil;

    public CommentController(CommentService commentService, UserRepository userRepository, UserUtil userUtil) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentDto commentDto) {
        try {
            return ResponseEntity.ok(commentService.write(commentDto, userUtil.getLoggedInUser()));
        } catch (NotLoggedInException | NotSearchedTargetException | NotQualifiedDtoException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping
    public ResponseEntity<?> editComment(@RequestBody EditCommentDto editCommentDto) {
        try {
            return ResponseEntity.ok(commentService.edit(editCommentDto, userUtil.getLoggedInUser()));
        } catch (NotLoggedInException | NotQualifiedDtoException | NotSearchedTargetException
                 | NotPermissionException e) {
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
