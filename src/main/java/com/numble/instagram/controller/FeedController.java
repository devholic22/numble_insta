package com.numble.instagram.controller;

import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.exception.NotSearchedTargetException;
import com.numble.instagram.service.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
public class FeedController {

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    private final FeedService feedService;

    @GetMapping("/{user_id}")
    @ResponseBody
    public ResponseEntity<?> room(@PathVariable Long user_id) {
        try {
            return ResponseEntity.ok(feedService.getFeed(user_id));
        }  catch (NotSearchedTargetException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }
}
