package com.numble.instagram.controller;

import com.numble.instagram.dto.DeleteReplyDto;
import com.numble.instagram.dto.EditReplyDto;
import com.numble.instagram.dto.ReplyDto;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;
    private final UserRepository userRepository;

    public ReplyController(ReplyService replyService, UserRepository userRepository) {
        this.replyService = replyService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> write(@RequestBody ReplyDto replyDto) {
        User loggedInUser = getLoggedInUser();
        try {
            return ResponseEntity.ok(replyService.write(replyDto, loggedInUser));
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping
    public ResponseEntity<?> edit(@RequestBody EditReplyDto editReplyDto) {
        User loggedInUser = getLoggedInUser();
        try {
            return ResponseEntity.ok(replyService.edit(editReplyDto, loggedInUser));
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody DeleteReplyDto deleteReplyDto) {
        User loggedInUser = getLoggedInUser();
        try {
            replyService.delete(deleteReplyDto, loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).build();
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
