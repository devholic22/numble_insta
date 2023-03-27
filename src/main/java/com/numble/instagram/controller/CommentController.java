package com.numble.instagram.controller;

import com.numble.instagram.dto.CommentDto;
import com.numble.instagram.entity.Comment;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    public CommentController(CommentService commentService, UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
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

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return userRepository.findByNickname(nickname);
    }
}
