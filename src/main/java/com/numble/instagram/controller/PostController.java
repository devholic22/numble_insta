package com.numble.instagram.controller;

import com.numble.instagram.dto.post.PostDto;
import com.numble.instagram.entity.Post;
import com.numble.instagram.entity.User;
import com.numble.instagram.exception.ExceptionResponse;
import com.numble.instagram.repository.PostRepository;
import com.numble.instagram.repository.UserRepository;
import com.numble.instagram.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostController(PostService postService,
                          UserRepository userRepository,
                          PostRepository postRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PostMapping
    // @RequestBody는 json
    // @RequestParam은 주로 html form에서 사용
    // @RequestPart는 파일 전송에 특화

    public ResponseEntity<Post> write(@ModelAttribute PostDto postDto) {
        return ResponseEntity.ok(postService.write(postDto, getLoggedInUser()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody PostDto postDto, @PathVariable Long id) {
        User loggedUser = getLoggedInUser();
        Optional<Post> targetPost = postRepository.findById(id);
        if (targetPost.isEmpty()) {
            ExceptionResponse exceptionResponse = new ExceptionResponse("해당 글이 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
        Post target = targetPost.get();
        User writer = target.getWriter();
        if (!writer.equals(loggedUser)) {
            ExceptionResponse exceptionResponse = new ExceptionResponse("글을 수정할 수 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(exceptionResponse);
        }
        return ResponseEntity.ok(postService.edit(target, postDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User loggedUser = getLoggedInUser();
        Optional<Post> targetPost = postRepository.findById(id);
        if (targetPost.isEmpty()) {
            ExceptionResponse exceptionResponse = new ExceptionResponse("해당 글이 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
        Post target = targetPost.get();
        User writer = target.getWriter();
        if (!writer.equals(loggedUser)) {
            ExceptionResponse exceptionResponse = new ExceptionResponse("글을 삭제할 수 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exceptionResponse);
        }
        postService.delete(target.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nickname = authentication.getName();
        return userRepository.findByNickname(nickname);
    }
}
