package com.numble.instagram.controller;

import com.numble.instagram.dto.FollowDto;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public ResponseEntity<?> followRequest(@RequestBody FollowDto followDto) {
        try {
            followService.addFollow(followDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> followCancel(@RequestBody FollowDto followDto) {
        try {
            followService.cancelFollow(followDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
