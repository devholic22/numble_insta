package com.numble.instagram.controller;

import com.numble.instagram.dto.post.PostDto;
import com.numble.instagram.exception.*;
import com.numble.instagram.repository.PostRepository;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.PostService;
import com.numble.instagram.util.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserUtil userUtil;

    public PostController(PostService postService,
                          UserUtil userUtil) {
        this.postService = postService;
        this.userUtil = userUtil;
    }

    // @RequestBody는 json
    // @RequestParam은 주로 html form에서 사용
    // @RequestPart는 파일 전송에 특화
    @PostMapping
    public ResponseEntity<?> write(@ModelAttribute PostDto postDto) {
        try {
            return ResponseEntity.ok(postService.write(postDto, userUtil.getLoggedInUser()));
        } catch (ExitedUserException | NotQualifiedDtoException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@ModelAttribute PostDto postDto, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(postService.edit(id, postDto, userUtil.getLoggedInUser()));
        } catch (ExitedUserException |
                 NotSearchedTargetException |
                 NotPermissionException |
                 NotQualifiedDtoException e) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            postService.delete(id, userUtil.getLoggedInUser());
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
