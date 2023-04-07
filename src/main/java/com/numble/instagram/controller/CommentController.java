package com.numble.instagram.controller;

import com.numble.instagram.dto.comment.CommentDto;
import com.numble.instagram.dto.comment.EditCommentDto;
import com.numble.instagram.exception.*;
import com.numble.instagram.service.CommentService;
import com.numble.instagram.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserUtil userUtil;

    public CommentController(CommentService commentService, UserUtil userUtil) {
        this.commentService = commentService;
        this.userUtil = userUtil;
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentDto commentDto) {
        try {
            return ResponseEntity.ok(commentService.write(commentDto, userUtil.getLoggedInUser()));
        } catch (NotLoggedInException |
                 ExitedUserException |
                 NotSearchedTargetException |
                 NotQualifiedDtoException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editComment(@RequestBody EditCommentDto editCommentDto, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(commentService.edit(editCommentDto, id, userUtil.getLoggedInUser()));
        } catch (NotLoggedInException |
                 ExitedUserException |
                 NotQualifiedDtoException |
                 NotSearchedTargetException |
                 NotPermissionException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.delete(id, userUtil.getLoggedInUser());
        } catch (NotLoggedInException |
                 ExitedUserException |
                 NotSearchedTargetException |
                 NotPermissionException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
