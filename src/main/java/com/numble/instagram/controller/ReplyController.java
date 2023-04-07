package com.numble.instagram.controller;

import com.numble.instagram.dto.reply.DeleteReplyDto;
import com.numble.instagram.dto.reply.EditReplyDto;
import com.numble.instagram.dto.reply.ReplyDto;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.ReplyService;
import com.numble.instagram.util.UserUtil;
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
    private final UserUtil userUtil;

    public ReplyController(ReplyService replyService, UserRepository userRepository, UserUtil userUtil) {
        this.replyService = replyService;
        this.userRepository = userRepository;
        this.userUtil = userUtil;
    }

    @PostMapping
    public ResponseEntity<?> write(@RequestBody ReplyDto replyDto) {
        try {
            return ResponseEntity.ok(replyService.write(replyDto, userUtil.getLoggedInUser()));
        } catch (NotLoggedInException | NotQualifiedDtoException | NotSearchedTargetException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody EditReplyDto editReplyDto, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(replyService.edit(editReplyDto, id, userUtil.getLoggedInUser()));
        } catch (NotLoggedInException |
                NotQualifiedDtoException | 
                NotSearchedTargetException |
                 NotPermissionException e) {
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
