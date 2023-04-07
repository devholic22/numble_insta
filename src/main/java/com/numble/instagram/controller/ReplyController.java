package com.numble.instagram.controller;

import com.numble.instagram.dto.reply.EditReplyDto;
import com.numble.instagram.dto.reply.ReplyDto;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.ReplyService;
import com.numble.instagram.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;
    private final UserUtil userUtil;

    public ReplyController(ReplyService replyService, UserUtil userUtil) {
        this.replyService = replyService;
        this.userUtil = userUtil;
    }

    @PostMapping
    public ResponseEntity<?> write(@RequestBody ReplyDto replyDto) {
        try {
            return ResponseEntity.ok(replyService.write(replyDto, userUtil.getLoggedInUser()));
        } catch (ExitedUserException |
                 NotQualifiedDtoException |
                 NotSearchedTargetException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody EditReplyDto editReplyDto, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(replyService.edit(editReplyDto, id, userUtil.getLoggedInUser()));
        } catch (ExitedUserException |
                 NotQualifiedDtoException |
                 NotSearchedTargetException |
                 NotPermissionException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            replyService.delete(id, userUtil.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ExitedUserException |
                 NotSearchedTargetException |
                 NotPermissionException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
