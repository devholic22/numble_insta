package com.numble.instagram.controller;

import com.numble.instagram.exception.*;
import com.numble.instagram.service.FollowService;
import com.numble.instagram.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final UserUtil userUtil;

    public FollowController(FollowService followService, UserUtil userUtil) {
        this.followService = followService;
        this.userUtil = userUtil;
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<?> followRequest(@PathVariable Long user_id) {
        try {
            followService.addFollow(user_id, userUtil.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotLoggedInException |
                 SelfFollowAPIException |
                 NotSearchedTargetException |
                 AlreadyFollowException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> followCancel(@PathVariable Long user_id) {
        try {
            followService.cancelFollow(user_id, userUtil.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotLoggedInException |
                 SelfFollowAPIException |
                 NotSearchedTargetException |
                 NotFollowException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
